package com.example.trading.user.api.v1.mapper;

import org.springframework.stereotype.Component;

import com.example.trading.user.api.v1.response.UserV1Response;
import com.example.trading.user.domain.model.User;

@Component
public class UserV1Mapper {

	public UserV1Response toResponse(User user) {
		return new UserV1Response(user.getId(), user.getName(), user.getEmail());
	}
}
