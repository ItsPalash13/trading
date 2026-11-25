package com.example.trading.user.application.port.in;

import com.example.trading.user.application.command.CreateUserCommand;
import com.example.trading.user.application.result.UserView;

/**
 * Port in — capability this module exposes: create a user.
 * Other modules (auth) and adapters call this; they never touch User's repository.
 */
public interface CreateUserUseCase {

	UserView create(CreateUserCommand command);
}
