package com.example.trading.platform.diagnostics.jfr;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator/jfr")
public class JfrEndpoint {

	private final FlightRecorderService recordings;

	public JfrEndpoint(FlightRecorderService recordings) {
		this.recordings = recordings;
	}

	@GetMapping
	public FlightRecorderService.Status status() {
		return recordings.status();
	}

	@PostMapping
	public FlightRecorderService.Dump dump() throws IOException {
		return recordings.dump();
	}
}
