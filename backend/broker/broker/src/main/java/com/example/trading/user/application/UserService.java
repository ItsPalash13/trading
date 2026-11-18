package com.example.trading.user.application;

import org.springframework.stereotype.Service;

import com.example.trading.user.domain.model.User;
import com.example.trading.user.domain.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public User requireByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("User not found: " + email));
	}
}
