package com.example.trading.auth.adapter.out.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.trading.auth.application.port.out.PasswordHasher;

/**
 * Adapter out (security) — app drives a Security library.
 * <p>
 * Lives under {@code adapter/out/security}: implements {@link PasswordHasher}
 * with BCrypt. Same Spring Security tech as {@code adapter/in/security}, opposite
 * direction (out = we call it; in = it calls us).
 */
@Component
public class SpringPasswordHasher implements PasswordHasher {

	private final PasswordEncoder passwordEncoder;

	public SpringPasswordHasher(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String hash(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
}
