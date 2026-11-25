package com.example.trading.user.application.result;

import com.example.trading.user.domain.model.User;

/**
 * Result — use-case output (application DTO). Safe to share across modules.
 * Not the domain {@link User} (no password hash / behavior) and not an HTTP Response.
 */
public record UserView(Long id, String name, String email) {

	public static UserView from(User user) {
		return new UserView(user.getId(), user.getName(), user.getEmail());
	}
}
