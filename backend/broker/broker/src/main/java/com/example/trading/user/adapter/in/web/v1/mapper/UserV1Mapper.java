package com.example.trading.user.adapter.in.web.v1.mapper;

import org.springframework.stereotype.Component;

import com.example.trading.user.adapter.in.web.v1.response.UserV1Response;
import com.example.trading.user.application.result.UserView;

@Component
public class UserV1Mapper {

	public UserV1Response toResponse(UserView user) {
		return new UserV1Response(user.id(), user.name(), user.email());
	}
}
