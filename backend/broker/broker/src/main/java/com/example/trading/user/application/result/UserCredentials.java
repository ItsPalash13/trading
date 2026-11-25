package com.example.trading.user.application.result;

public record UserCredentials(Long userId, String email, String passwordHash) {
}
