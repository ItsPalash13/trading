package com.example.trading.platform.diagnostics.jfr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import jdk.jfr.Configuration;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

class JfrEventRecordingTest {

	@Test
	void authAttemptIsWrittenForJmc() throws Exception {
		Path dir = Files.createTempDirectory("broker-jfr");
		Path file = dir.resolve("auth.jfr");
		try (Recording recording = new Recording(Configuration.getConfiguration("default"))) {
			recording.enable(AuthAttemptEvent.class).withoutStackTrace();
			recording.start();
			JfrEvents.auth("login", false, "rejected");
			recording.stop();
			recording.dump(file);
		}

		boolean found = false;
		try (RecordingFile recorded = new RecordingFile(file)) {
			while (recorded.hasMoreEvents()) {
				RecordedEvent event = recorded.readEvent();
				if (!"trading.auth.Attempt".equals(event.getEventType().getName())) {
					continue;
				}
				found = true;
				assertEquals("login", event.getString("action"));
				assertEquals(false, event.getBoolean("success"));
				assertEquals("rejected", event.getString("detail"));
			}
		}
		assertTrue(found, "trading.auth.Attempt was not in the recording");
	}

	@Test
	void httpExchangeIncludesStackTraceWhenEnabled() throws Exception {
		Path dir = Files.createTempDirectory("broker-jfr");
		Path file = dir.resolve("http.jfr");
		try (Recording recording = new Recording(Configuration.getConfiguration("default"))) {
			recording.enable(HttpExchangeEvent.class).withStackTrace();
			recording.start();
			HttpExchangeEvent event = new HttpExchangeEvent();
			event.begin();
			event.method = "GET";
			event.path = "/actuator/jfr";
			event.status = 200;
			event.outcome = "SUCCESS";
			event.commit();
			recording.stop();
			recording.dump(file);
		}

		boolean found = false;
		try (RecordingFile recorded = new RecordingFile(file)) {
			while (recorded.hasMoreEvents()) {
				RecordedEvent event = recorded.readEvent();
				if (!"trading.http.Exchange".equals(event.getEventType().getName())) {
					continue;
				}
				found = true;
				assertEquals("GET", event.getString("method"));
				assertTrue(event.getStackTrace() != null && !event.getStackTrace().getFrames().isEmpty());
			}
		}
		assertTrue(found, "trading.http.Exchange was not in the recording");
	}
}
