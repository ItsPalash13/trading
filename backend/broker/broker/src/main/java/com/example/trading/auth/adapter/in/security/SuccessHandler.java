package com.example.trading.auth.adapter.in.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.trading.account.adapter.in.web.CurrentAccountSession;
import com.example.trading.auth.application.port.in.SessionUseCase;
import com.example.trading.auth.application.result.SessionView;
import com.example.trading.platform.diagnostics.jfr.JfrEvents;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

	private final SessionUseCase sessionUseCase;
	private final CurrentAccountSession currentAccountSession;

	public SuccessHandler(SessionUseCase sessionUseCase, CurrentAccountSession currentAccountSession) {
		this.sessionUseCase = sessionUseCase;
		this.currentAccountSession = currentAccountSession;
	}

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		SessionView session = sessionUseCase.startSession(authentication.getName());
		currentAccountSession.set(request.getSession(true), session.currentAccount().id());
		JfrEvents.auth("login", true, "authenticated");
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write("{\"authenticated\":true}");
	}
}
