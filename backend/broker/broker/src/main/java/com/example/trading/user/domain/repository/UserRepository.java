package com.example.trading.user.domain.repository;

import java.util.Optional;

import com.example.trading.user.domain.model.User;

public interface UserRepository {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	User save(User user);
}
