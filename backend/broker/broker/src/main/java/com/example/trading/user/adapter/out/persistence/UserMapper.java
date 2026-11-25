package com.example.trading.user.adapter.out.persistence;

import com.example.trading.user.domain.model.User;

final class UserMapper {

	private UserMapper() {
	}

	static UserJpaEntity toEntity(User user) {
		return new UserJpaEntity(user.getId(), user.getEmail(), user.getPasswordHash(), user.getName());
	}

	static User toDomain(UserJpaEntity entity) {
		return User.restore(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getName());
	}
}
