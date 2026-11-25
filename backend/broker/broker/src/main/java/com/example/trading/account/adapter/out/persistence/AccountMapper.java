package com.example.trading.account.adapter.out.persistence;

import com.example.trading.account.domain.model.Account;

final class AccountMapper {

	private AccountMapper() {
	}

	static AccountJpaEntity toEntity(Account account) {
		return new AccountJpaEntity(
				account.getId(),
				account.getUserId(),
				account.getType(),
				account.getName(),
				account.getCreatedAt());
	}

	static Account toDomain(AccountJpaEntity entity) {
		return Account.restore(
				entity.getId(),
				entity.getUserId(),
				entity.getType(),
				entity.getName(),
				entity.getCreatedAt());
	}
}
