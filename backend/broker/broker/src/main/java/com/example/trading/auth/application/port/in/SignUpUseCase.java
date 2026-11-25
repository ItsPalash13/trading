package com.example.trading.auth.application.port.in;

import com.example.trading.auth.application.command.SignUpCommand;
import com.example.trading.user.application.result.UserView;

/**
 * Port in — “what can you ask this module to do?”
 * <p>
 * Inbound port: the capability auth exposes (sign up). Adapters (e.g. AuthController)
 * call this; {@code SignUpService} implements it.
 */
public interface SignUpUseCase {

	UserView signUp(SignUpCommand command);
}
