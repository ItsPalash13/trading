package com.example.trading.platform.diagnostics.jfr;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

@SpringBootTest(properties = {
		"diagnostics.jfr.enabled=true",
		"diagnostics.jfr.directory=target/jfr-test",
		"diagnostics.jfr.name=broker-test",
		"diagnostics.jfr.http.stack-trace=true",
		"logging.level.org.springframework.security=WARN"
})
@AutoConfigureMockMvc
class JfrEndpointTest {

	@Autowired
	private MockMvc mvc;

	@Test
	void statusAndDumpExposeTheRecording() throws Exception {
		mvc.perform(get("/actuator/jfr").with(request -> {
			request.setRemoteAddr("127.0.0.1");
			return request;
		}))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state").value("RUNNING"))
				.andExpect(jsonPath("$.name").value("broker-test"));

		MvcResult dumped = mvc.perform(post("/actuator/jfr").with(request -> {
			request.setRemoteAddr("127.0.0.1");
			return request;
		}))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.path").exists())
				.andExpect(jsonPath("$.sizeBytes").isNumber())
				.andReturn();

		String path = com.jayway.jsonpath.JsonPath.read(dumped.getResponse().getContentAsString(), "$.path");
		boolean found = false;
		boolean stackTraceFound = false;
		try (RecordingFile recorded = new RecordingFile(Path.of(path))) {
			while (recorded.hasMoreEvents()) {
				RecordedEvent event = recorded.readEvent();
				if (!"trading.http.Exchange".equals(event.getEventType().getName())
						|| !"/actuator/jfr".equals(event.getString("path"))) {
					continue;
				}
				found = true;
				if (event.getStackTrace() != null && !event.getStackTrace().getFrames().isEmpty()) {
					stackTraceFound = true;
					break;
				}
			}
		}
		assertTrue(found, "trading.http.Exchange for /actuator/jfr was not in the dump");
		assertTrue(stackTraceFound, "trading.http.Exchange did not include a stack trace");
	}
}
