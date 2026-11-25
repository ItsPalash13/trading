package com.example.trading.user.domain.model;

import java.util.Locale;

import com.example.trading.platform.exception.ValidationException;

/**
 * Domain entity — identity + invariants (normalize email, require fields).
 * <p>
 * There is no separate Domain Service in this module: rules fit on the entity.
 * Add a domain service only when logic spans multiple entities and belongs
 * nowhere else. Domain must not call repositories or ports.
 */
public class User {

	private final Long id;
	private final String email;
	private final String passwordHash;
	private final String name;

	private User(Long id, String email, String passwordHash, String name) {
		this.id = id;
		this.email = email;
		this.passwordHash = passwordHash;
		this.name = name;
	}

	public static User register(String email, String passwordHash, String name) {
		requireText(email, "Email is required");
		requireText(passwordHash, "Password is required");
		requireText(name, "Name is required");
		return new User(null, normalizeEmail(email), passwordHash, name.trim());
	}

	public static User restore(Long id, String email, String passwordHash, String name) {
		return new User(id, email, passwordHash, name);
	}

	public static String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	private static void requireText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new ValidationException(message);
		}
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getName() {
		return name;
	}
}
