package com.example.trading.auth.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trading.account.application.port.in.OpenAccountUseCase;
import com.example.trading.auth.application.command.SignUpCommand;
import com.example.trading.auth.application.port.in.SignUpUseCase;
import com.example.trading.auth.application.port.out.PasswordHasher;
import com.example.trading.platform.diagnostics.jfr.JfrEvents;
import com.example.trading.platform.exception.ConflictException;
import com.example.trading.platform.exception.ValidationException;
import com.example.trading.user.application.command.CreateUserCommand;
import com.example.trading.user.application.port.in.CreateUserUseCase;
import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.result.UserView;

/**
 * Application service — the hub of the onion for sign-up.
 * <p>
 * Flow (not a full reverse through domain):
 * <pre>
 *   Adapter in → Port in → this service
 *                              ├──► other modules' Port in (user, account)
 *                              └──► Port out (PasswordHasher) → Adapter out
 * </pre>
 * Auth has no domain package; rules for User/Account live in those modules.
 * Owns the consistency boundary: user + Regular account in one transaction.
 */
@Service
public class SignUpService implements SignUpUseCase {

	private final UserQuery userQuery;
	private final CreateUserUseCase createUser;
	private final OpenAccountUseCase openAccount;
	private final PasswordHasher passwordHasher;

	public SignUpService(
			UserQuery userQuery,
			CreateUserUseCase createUser,
			OpenAccountUseCase openAccount,
			PasswordHasher passwordHasher) {
		this.userQuery = userQuery;
		this.createUser = createUser;
		this.openAccount = openAccount;
		this.passwordHasher = passwordHasher;
	}

	@Override
	@Transactional
	public UserView signUp(SignUpCommand command) {
		if (isBlank(command.name()) || isBlank(command.email()) || isBlank(command.password())) {
			JfrEvents.auth("register", false, "missing fields");
			throw new ValidationException("Name, email and password are required");
		}

		if (userQuery.existsByEmail(command.email())) {
			JfrEvents.auth("register", false, "conflict");
			throw new ConflictException("Email already registered");
		}

		UserView user = createUser.create(new CreateUserCommand(
				command.email(),
				passwordHasher.hash(command.password()),
				command.name()));
		openAccount.openRegularAccount(user.id());
		JfrEvents.auth("register", true, "registered");
		return user;
	}

	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}
}
