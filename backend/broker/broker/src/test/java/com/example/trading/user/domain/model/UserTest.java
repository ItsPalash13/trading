package com.example.trading.user.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.example.trading.platform.exception.ValidationException;

class UserTest {

	@Test
	void registerNormalizesEmailAndTrimsName() {
		User user = User.register("  Alice@Example.COM ", "hash", "  Alice ");

		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("alice@example.com");
		assertThat(user.getName()).isEqualTo("Alice");
		assertThat(user.getPasswordHash()).isEqualTo("hash");
	}

	@Test
	void registerRejectsBlankFields() {
		assertThatThrownBy(() -> User.register(" ", "hash", "Alice")).isInstanceOf(ValidationException.class);
		assertThatThrownBy(() -> User.register("a@b.com", null, "Alice")).isInstanceOf(ValidationException.class);
		assertThatThrownBy(() -> User.register("a@b.com", "hash", "")).isInstanceOf(ValidationException.class);
	}
}
