package com.example.trading.user.application.command;

/**
 * Command — input to {@code CreateUserUseCase}. Application-layer data only
 * (already-hashed password); not a web Request DTO.
 */
public record CreateUserCommand(String email, String passwordHash, String name) {
}
