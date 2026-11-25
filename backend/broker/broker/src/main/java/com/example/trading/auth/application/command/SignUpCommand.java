package com.example.trading.auth.application.command;

/**
 * Command — use-case input (application DTO).
 * Not an HTTP DTO: AuthController maps {@code RegisterRequest} → this, then
 * calls {@code SignUpUseCase}.
 */
public record SignUpCommand(String name, String email, String password) {
}
