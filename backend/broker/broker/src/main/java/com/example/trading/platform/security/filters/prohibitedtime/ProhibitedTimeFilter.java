package com.example.trading.platform.security.filters.prohibitedtime;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProhibitedTimeFilter extends OncePerRequestFilter {

	private final ProhibitedTimeProperties properties;

	public ProhibitedTimeFilter(ProhibitedTimeProperties properties) {
		this.properties = properties;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		if (properties.isEnabled() && isProhibitedNow()) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(
					"{\"error\":\"Forbidden\",\"message\":\"Access is blocked during the prohibited time period\"}");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean isProhibitedNow() {
		LocalTime start = properties.getStart();
		LocalTime end = properties.getEnd();
		if (start.equals(end)) {
			return false;
		}

		LocalTime now = LocalTime.now(ZoneId.of(properties.getZoneId()));

		// Same-day window, e.g. 09:00–17:00
		if (start.isBefore(end)) {
			return !now.isBefore(start) && now.isBefore(end);
		}

		// Overnight window, e.g. 22:00–06:00
		return !now.isBefore(start) || now.isBefore(end);
	}
}
