package com.example.trading.platform.security.filters.prohibitedtime;

import java.time.LocalTime;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.prohibited-time")
public class ProhibitedTimeProperties {

	private boolean enabled = false;
	private LocalTime start = LocalTime.MIDNIGHT;
	private LocalTime end = LocalTime.MIDNIGHT;
	private String zoneId = "Asia/Kolkata";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalTime getStart() {
		return start;
	}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
