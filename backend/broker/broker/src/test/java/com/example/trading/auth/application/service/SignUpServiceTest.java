package com.example.trading.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.example.trading.account.application.port.in.OpenAccountUseCase;
import com.example.trading.auth.application.command.SignUpCommand;
import com.example.trading.auth.application.port.out.PasswordHasher;
import com.example.trading.platform.exception.ConflictException;
import com.example.trading.platform.exception.ValidationException;
import com.example.trading.user.application.command.CreateUserCommand;
import com.example.trading.user.application.port.in.CreateUserUseCase;
import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.result.UserView;

class SignUpServiceTest {

	private final UserQuery userQuery = mock(UserQuery.class);
	private final CreateUserUseCase createUser = mock(CreateUserUseCase.class);
	private final OpenAccountUseCase openAccount = mock(OpenAccountUseCase.class);
	private final PasswordHasher passwordHasher = raw -> "hashed:" + raw;
	private final SignUpService service = new SignUpService(userQuery, createUser, openAccount, passwordHasher);

	@Test
	void signUpCreatesUserWithHashedPasswordAndOpensRegularAccount() {
		UserView created = new UserView(1L, "Alice", "alice@example.com");
		when(createUser.create(new CreateUserCommand("alice@example.com", "hashed:secret", "Alice")))
				.thenReturn(created);

		UserView result = service.signUp(new SignUpCommand("Alice", "alice@example.com", "secret"));

		assertThat(result).isEqualTo(created);
		verify(openAccount).openRegularAccount(1L);
	}

	@Test
	void signUpRejectsMissingFields() {
		assertThatThrownBy(() -> service.signUp(new SignUpCommand("Alice", "alice@example.com", " ")))
				.isInstanceOf(ValidationException.class);
		verify(createUser, never()).create(any());
	}

	@Test
	void signUpRejectsExistingEmail() {
		when(userQuery.existsByEmail("alice@example.com")).thenReturn(true);

		assertThatThrownBy(() -> service.signUp(new SignUpCommand("Alice", "alice@example.com", "secret")))
				.isInstanceOf(ConflictException.class);
		verify(openAccount, never()).openRegularAccount(any());
	}
}
