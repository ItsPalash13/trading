package com.example.trading.user.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.trading.platform.exception.NotFoundException;
import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.port.out.UserRepository;
import com.example.trading.user.application.result.UserCredentials;
import com.example.trading.user.application.result.UserView;
import com.example.trading.user.domain.model.User;

@Service
public class UserQueryService implements UserQuery {

	private final UserRepository userRepository;

	public UserQueryService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(User.normalizeEmail(email));
	}

	@Override
	public Optional<UserView> findByEmail(String email) {
		return userRepository.findByEmail(email).map(UserView::from);
	}

	@Override
	public UserView getByEmail(String email) {
		return findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
	}

	@Override
	public Optional<UserCredentials> findCredentialsByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(user -> new UserCredentials(user.getId(), user.getEmail(), user.getPasswordHash()));
	}
}
