package com.example.trading.account.domain.model;

import java.time.Instant;

import com.example.trading.platform.exception.ForbiddenException;
import com.example.trading.platform.exception.ValidationException;

/**
 * Domain entity — ownership and open-regular rules live here (not in a Domain Service).
 * Application services call these methods; this class never calls repositories.
 */
public class Account {

	private final Long id;
	private final Long userId;
	private final AccountType type;
	private final String name;
	private final Instant createdAt;

	private Account(Long id, Long userId, AccountType type, String name, Instant createdAt) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static Account openRegular(Long userId) {
		if (userId == null) {
			throw new ValidationException("Account owner is required");
		}
		return new Account(null, userId, AccountType.REGULAR, "Regular", Instant.now());
	}

	public static Account restore(Long id, Long userId, AccountType type, String name, Instant createdAt) {
		return new Account(id, userId, type, name, createdAt);
	}

	public boolean isOwnedBy(Long userId) {
		return this.userId.equals(userId);
	}

	public void ensureOwnedBy(Long userId) {
		if (!isOwnedBy(userId)) {
			throw new ForbiddenException("Account does not belong to user");
		}
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public AccountType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
