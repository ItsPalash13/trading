package com.example.trading.account.adapter.out.persistence;

import java.time.Instant;

import com.example.trading.account.domain.model.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 32)
	private AccountType type;

	@Column(nullable = false)
	private String name;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	protected AccountJpaEntity() {
	}

	AccountJpaEntity(Long id, Long userId, AccountType type, String name, Instant createdAt) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.name = name;
		this.createdAt = createdAt;
	}

	Long getId() {
		return id;
	}

	Long getUserId() {
		return userId;
	}

	AccountType getType() {
		return type;
	}

	String getName() {
		return name;
	}

	Instant getCreatedAt() {
		return createdAt;
	}
}
