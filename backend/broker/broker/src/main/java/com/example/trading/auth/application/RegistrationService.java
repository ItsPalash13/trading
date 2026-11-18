package com.example.trading.auth.application;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.trading.user.domain.model.User;
import com.example.trading.user.domain.repository.UserRepository;

@Service
public class RegistrationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User register(String name, String email, String password) {
		if (name == null || name.isBlank()
				|| email == null || email.isBlank()
				|| password == null || password.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, email and password are required");
		}

		String normalizedEmail = email.trim().toLowerCase();
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
		}

		User user = new User(
				normalizedEmail,
				passwordEncoder.encode(password),
				name.trim());

		return userRepository.save(user);
	}
}
