package com.example.trading.auth.api.mapper;

import org.springframework.stereotype.Component;

import com.example.trading.auth.api.response.RegisterResponse;
import com.example.trading.auth.api.response.SessionResponse;
import com.example.trading.user.domain.model.User;

@Component
public class AuthMapper {

	public RegisterResponse toRegisterResponse(User user) {
		return new RegisterResponse(user.getId(), user.getName(), user.getEmail());
	}

	public SessionResponse toSessionResponse(User user) {
		return new SessionResponse(user.getId(), user.getName(), user.getEmail());
	}
}
