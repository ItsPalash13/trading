package com.example.trading.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trading.platform.exception.ConflictException;
import com.example.trading.user.application.command.CreateUserCommand;
import com.example.trading.user.application.port.in.CreateUserUseCase;
import com.example.trading.user.application.port.out.UserRepository;
import com.example.trading.user.application.result.UserView;
import com.example.trading.user.domain.model.User;

@Service
public class CreateUserService implements CreateUserUseCase {

	private final UserRepository userRepository;

	public CreateUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserView create(CreateUserCommand command) {
		User user = User.register(command.email(), command.passwordHash(), command.name());
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new ConflictException("Email already registered");
		}
		return UserView.from(userRepository.save(user));
	}
}
