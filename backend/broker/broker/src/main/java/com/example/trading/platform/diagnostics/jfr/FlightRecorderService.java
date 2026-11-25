package com.example.trading.platform.diagnostics.jfr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jdk.jfr.Configuration;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;

@Component
public class FlightRecorderService {

	private static final Logger log = LoggerFactory.getLogger(FlightRecorderService.class);
	private static final DateTimeFormatter DUMP_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

	private final JfrProperties properties;
	private volatile Recording active;
	private Recording owned;

	public FlightRecorderService(JfrProperties properties) {
		this.properties = properties;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady() {
		if (!properties.isEnabled()) {
			log.debug("JFR recording is disabled");
			return;
		}
		synchronized (this) {
			if (active != null) {
				return;
			}
			try {
				Recording existing = findRunning(properties.getName());
				if (existing != null) {
					prepare(existing);
					active = existing;
					logStarted("Attached to the existing JFR recording");
					return;
				}
				Recording created = create();
				created.start();
				owned = created;
				active = created;
				logStarted("Started JFR recording");
			}
			catch (IOException | ParseException ex) {
				throw new IllegalStateException("Could not start Java Flight Recorder", ex);
			}
		}
	}

	@PreDestroy
	public void stopOwnedRecording() {
		Recording recording;
		synchronized (this) {
			recording = owned;
			owned = null;
			if (recording != null) {
				active = null;
			}
		}
		if (recording == null) {
			return;
		}
		try {
			if (recording.getState() == RecordingState.RUNNING) {
				recording.stop();
			}
		}
		finally {
			recording.close();
		}
	}

	public Status status() {
		Recording recording = active;
		if (recording == null) {
			return new Status(
					properties.isEnabled(),
					properties.getName(),
					"NONE",
					0,
					null,
					false,
					properties.getSettings());
		}
		Path destination = recording.getDestination();
		return new Status(
				properties.isEnabled(),
				recording.getName(),
				recording.getState().name(),
				recording.getSize(),
				destination == null ? null : destination.toAbsolutePath().toString(),
				recording.getDumpOnExit(),
				properties.getSettings());
	}

	public Dump dump() throws IOException {
		synchronized (this) {
			Recording recording = active;
			if (recording == null || recording.getState() != RecordingState.RUNNING) {
				throw new IllegalStateException("No running JFR recording");
			}
			Files.createDirectories(directory());
			String timestamp = DUMP_TIMESTAMP.format(LocalDateTime.now());
			Path path = directory().resolve(properties.getName() + "-" + timestamp + ".jfr");
			recording.dump(path);
			return new Dump(path.toAbsolutePath().toString(), Files.size(path));
		}
	}

	private Recording create() throws IOException, ParseException {
		Recording recording = new Recording(loadConfiguration());
		recording.setName(properties.getName());
		prepare(recording);
		return recording;
	}

	private void prepare(Recording recording) throws IOException {
		if (recording.getState() == RecordingState.NEW) {
			recording.setToDisk(true);
		}
		recording.setMaxAge(properties.getMaxAge());
		recording.setMaxSize(properties.getMaxSize().toBytes());
		var http = recording.enable(HttpExchangeEvent.class);
		if (properties.getHttp().isStackTrace()) {
			http.withStackTrace();
		}
		else {
			http.withoutStackTrace();
		}
		recording.enable(AuthAttemptEvent.class).withoutStackTrace();
		Files.createDirectories(directory());
		if (properties.isDumpOnExit() && recording.getDestination() == null) {
			recording.setDumpOnExit(true);
			recording.setDestination(directory().resolve(properties.getName() + ".jfr"));
		}
	}

	private Configuration loadConfiguration() throws IOException, ParseException {
		Path path = Path.of(properties.getSettings());
		if (Files.isRegularFile(path)) {
			return Configuration.create(path);
		}
		return Configuration.getConfiguration(properties.getSettings());
	}

	private Recording findRunning(String name) {
		for (Recording recording : FlightRecorder.getFlightRecorder().getRecordings()) {
			if (name.equals(recording.getName()) && recording.getState() == RecordingState.RUNNING) {
				return recording;
			}
		}
		return null;
	}

	private Path directory() {
		return Path.of(properties.getDirectory());
	}

	private void logStarted(String action) {
		Recording recording = active;
		Path destination = recording.getDestination();
		String file = destination == null ? "(no dump file until a snapshot is taken)" : destination.toAbsolutePath().toString();
		log.info(
				"{} '{}' (pid {}). File: {}. Open it with scripts/jmc.ps1",
				action,
				recording.getName(),
				ProcessHandle.current().pid(),
				file);
	}

	public record Status(
			boolean enabled,
			String name,
			String state,
			long sizeBytes,
			String destination,
			boolean dumpOnExit,
			String settings) {
	}

	public record Dump(String path, long sizeBytes) {
	}
}
