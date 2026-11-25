package com.example.trading.user.application.port.in;

import java.util.Optional;

import com.example.trading.user.application.result.UserCredentials;
import com.example.trading.user.application.result.UserView;

/**
 * Port in — read capabilities this module exposes (lookup user / credentials).
 * Callers (auth, web) use this instead of importing User or UserRepository.
 */
public interface UserQuery {

	boolean existsByEmail(String email);

	Optional<UserView> findByEmail(String email);

	UserView getByEmail(String email);

	Optional<UserCredentials> findCredentialsByEmail(String email);
}
