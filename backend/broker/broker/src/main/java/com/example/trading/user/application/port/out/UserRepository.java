package com.example.trading.user.application.port.out;

import java.util.Optional;

import com.example.trading.user.domain.model.User;

/**
 * Port out — persistence capability the application needs.
 * Implemented by {@code UserPersistenceAdapter} (JPA); application code never
 * depends on Spring Data types.
 */
public interface UserRepository {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	User save(User user);
}
