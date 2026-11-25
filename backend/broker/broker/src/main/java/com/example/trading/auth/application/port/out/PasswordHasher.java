package com.example.trading.auth.application.port.out;

/**
 * Port out — “what does this module need from outside?”
 * <p>
 * Outbound port: auth needs hashing but must not depend on BCrypt.
 * {@code SpringPasswordHasher} (adapter out) implements this.
 */
public interface PasswordHasher {

	String hash(String rawPassword);
}
