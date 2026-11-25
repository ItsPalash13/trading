package com.example.trading.auth.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "diagnostics.jfr.enabled=false")
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void signUpLoginAndReadSession() throws Exception {
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Bob\",\"email\":\"Bob@Example.com\",\"password\":\"secret\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email").value("bob@example.com"))
				.andExpect(jsonPath("$.name").value("Bob"));

		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Bob\",\"email\":\"bob@example.com\",\"password\":\"secret\"}"))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Email already registered"));

		MockHttpSession session = new MockHttpSession();
		mockMvc.perform(post("/api/auth/login")
				.session(session)
				.param("username", "bob@example.com")
				.param("password", "secret"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.authenticated").value(true));

		mockMvc.perform(get("/api/auth/session").session(session))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("bob@example.com"))
				.andExpect(jsonPath("$.currentAccount.type").value("REGULAR"))
				.andExpect(jsonPath("$.currentAccount.name").value("Regular"));

		mockMvc.perform(get("/api/v1/users/me").session(session))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("bob@example.com"));
	}

	@Test
	void signUpWithMissingFieldsIsBadRequest() throws Exception {
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"\",\"email\":\"x@example.com\",\"password\":\"secret\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Name, email and password are required"));
	}
}
