package com.example.trading.platform.diagnostics.jfr;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpExchangeRecordingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		HttpExchangeEvent event = new HttpExchangeEvent();
		event.begin();
		int status = response.getStatus();
		try {
			filterChain.doFilter(request, response);
			status = response.getStatus();
		}
		catch (IOException | ServletException | RuntimeException ex) {
			int current = response.getStatus();
			status = current >= 400 ? current : 500;
			throw ex;
		}
		finally {
			event.method = request.getMethod();
			event.path = path(request);
			event.status = status;
			event.outcome = outcome(status);
			event.commit();
		}
	}

	private static String path(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.length() <= 256) {
			return uri;
		}
		return uri.substring(0, 256);
	}

	private static String outcome(int status) {
		if (status >= 500) {
			return "SERVER_ERROR";
		}
		if (status >= 400) {
			return "CLIENT_ERROR";
		}
		if (status >= 100) {
			return "SUCCESS";
		}
		return "UNKNOWN";
	}
}
