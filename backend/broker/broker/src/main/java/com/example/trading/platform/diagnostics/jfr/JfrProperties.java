package com.example.trading.platform.diagnostics.jfr;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "diagnostics.jfr")
public class JfrProperties {

	private boolean enabled = true;

	/**
	 * Must match {@code -XX:StartFlightRecording=name=...} in the Spring Boot Maven plugin.
	 */
	private String name = "broker";

	/** Built-in template ({@code default} or {@code profile}) or a path to a {@code .jfc} file. */
	private String settings = "profile";

	private String directory = "target/jfr";
	private Duration maxAge = Duration.ofMinutes(30);
	private DataSize maxSize = DataSize.ofMegabytes(256);
	private boolean dumpOnExit = true;
	private final Http http = new Http();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public Duration getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Duration maxAge) {
		this.maxAge = maxAge;
	}

	public DataSize getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(DataSize maxSize) {
		this.maxSize = maxSize;
	}

	public boolean isDumpOnExit() {
		return dumpOnExit;
	}

	public void setDumpOnExit(boolean dumpOnExit) {
		this.dumpOnExit = dumpOnExit;
	}

	public Http getHttp() {
		return http;
	}

	public static class Http {

		/** Capture a stack trace on every {@code trading.http.Exchange} event. */
		private boolean stackTrace = true;

		public boolean isStackTrace() {
			return stackTrace;
		}

		public void setStackTrace(boolean stackTrace) {
			this.stackTrace = stackTrace;
		}
	}
}
