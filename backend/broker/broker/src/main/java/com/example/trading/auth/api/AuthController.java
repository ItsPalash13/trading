package com.example.trading.auth.api;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading.auth.api.mapper.AuthMapper;
import com.example.trading.auth.api.request.RegisterRequest;
import com.example.trading.auth.api.response.RegisterResponse;
import com.example.trading.auth.api.response.SessionResponse;
import com.example.trading.auth.application.AuthService;
import com.example.trading.auth.application.RegistrationService;
import com.example.trading.user.domain.model.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final RegistrationService registrationService;
	private final AuthService authService;
	private final AuthMapper authMapper;

	public AuthController(
			RegistrationService registrationService,
			AuthService authService,
			AuthMapper authMapper) {
		this.registrationService = registrationService;
		this.authService = authService;
		this.authMapper = authMapper;
	}

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse signup(@RequestBody RegisterRequest request) {
		User user = registrationService.register(request.getName(), request.getEmail(), request.getPassword());
		return authMapper.toRegisterResponse(user);
	}

	@GetMapping("/session")
	public SessionResponse getSession(Principal principal) {
		return authMapper.toSessionResponse(authService.getSession(principal.getName()));
	}
}
