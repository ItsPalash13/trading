package com.example.trading.user.adapter.in.web.v1;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading.platform.config.ConditionalOnApiVersion;
import com.example.trading.user.adapter.in.web.v1.mapper.UserV1Mapper;
import com.example.trading.user.adapter.in.web.v1.response.UserV1Response;
import com.example.trading.user.application.port.in.UserQuery;

@RestController
@RequestMapping("/users")
@ConditionalOnApiVersion(module = "user", version = "v1")
public class UserV1Controller {

	private final UserQuery userQuery;
	private final UserV1Mapper userV1Mapper;

	public UserV1Controller(UserQuery userQuery, UserV1Mapper userV1Mapper) {
		this.userQuery = userQuery;
		this.userV1Mapper = userV1Mapper;
	}

	@GetMapping("/me")
	public UserV1Response me(Authentication authentication) {
		return userV1Mapper.toResponse(userQuery.getByEmail(authentication.getName()));
	}
}
