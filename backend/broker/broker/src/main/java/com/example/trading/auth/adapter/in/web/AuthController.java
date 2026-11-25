package com.example.trading.auth.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading.account.adapter.in.web.CurrentAccountSession;
import com.example.trading.auth.adapter.in.web.mapper.AuthMapper;
import com.example.trading.auth.adapter.in.web.request.RegisterRequest;
import com.example.trading.auth.adapter.in.web.response.RegisterResponse;
import com.example.trading.auth.adapter.in.web.response.SessionResponse;
import com.example.trading.auth.application.command.SignUpCommand;
import com.example.trading.auth.application.port.in.SessionUseCase;
import com.example.trading.auth.application.port.in.SignUpUseCase;
import com.example.trading.auth.application.result.SessionView;

import jakarta.servlet.http.HttpSession;

/**
 * Adapter in (web) — HTTP drives the app.
 * <p>
 * Converts Request DTOs → Command, calls port in ({@link SignUpUseCase} /
 * {@link SessionUseCase}), maps Result → Response DTOs. No business workflow here.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final SignUpUseCase signUpUseCase;
	private final SessionUseCase sessionUseCase;
	private final CurrentAccountSession currentAccountSession;
	private final AuthMapper authMapper;

	public AuthController(
			SignUpUseCase signUpUseCase,
			SessionUseCase sessionUseCase,
			CurrentAccountSession currentAccountSession,
			AuthMapper authMapper) {
		this.signUpUseCase = signUpUseCase;
		this.sessionUseCase = sessionUseCase;
		this.currentAccountSession = currentAccountSession;
		this.authMapper = authMapper;
	}

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse signup(@RequestBody RegisterRequest request) {
		SignUpCommand command = new SignUpCommand(request.getName(), request.getEmail(), request.getPassword());
		return authMapper.toRegisterResponse(signUpUseCase.signUp(command));
	}

	@GetMapping("/session")
	public SessionResponse getSession(Authentication authentication, HttpSession session) {
		Long selectedAccountId = currentAccountSession.get(session);
		SessionView view = sessionUseCase.resolveSession(authentication.getName(), selectedAccountId);
		if (selectedAccountId == null) {
			currentAccountSession.set(session, view.currentAccount().id());
		}
		return authMapper.toSessionResponse(view);
	}
}
