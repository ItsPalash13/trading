package com.example.trading.auth.adapter.in.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.trading.account.adapter.in.web.CurrentAccountSession;
import com.example.trading.platform.diagnostics.jfr.JfrEvents;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

	private final CurrentAccountSession currentAccountSession;

	public LogoutHandler(CurrentAccountSession currentAccountSession) {
		this.currentAccountSession = currentAccountSession;
	}

	@Override
	public void onLogoutSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		JfrEvents.auth("logout", true, "logged out");
		HttpSession session = request.getSession(false);
		if (session != null) {
			currentAccountSession.clear(session);
		}
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write("{\"authenticated\":false}");
	}
}
