package com.example.trading.user.api.v1;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trading.common.config.ConditionalOnApiVersion;
import com.example.trading.user.api.v1.mapper.UserV1Mapper;
import com.example.trading.user.api.v1.response.UserV1Response;
import com.example.trading.user.application.UserProfileService;

@RestController
@RequestMapping("/users")
@ConditionalOnApiVersion(module = "user", version = "v1")
public class UserV1Controller {

	private final UserProfileService userProfileService;
	private final UserV1Mapper userV1Mapper;

	public UserV1Controller(UserProfileService userProfileService, UserV1Mapper userV1Mapper) {
		this.userProfileService = userProfileService;
		this.userV1Mapper = userV1Mapper;
	}

	@GetMapping("/me")
	public UserV1Response me(Authentication authentication) {
		return userV1Mapper.toResponse(userProfileService.getProfile(authentication.getName()));
	}
}
