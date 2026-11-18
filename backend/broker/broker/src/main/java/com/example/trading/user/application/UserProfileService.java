package com.example.trading.user.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.trading.user.domain.model.User;
import com.example.trading.user.domain.repository.UserRepository;

@Service
public class UserProfileService {

	private final UserRepository userRepository;

	public UserProfileService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getProfile(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}
}
