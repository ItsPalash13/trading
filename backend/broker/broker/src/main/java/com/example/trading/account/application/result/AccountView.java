package com.example.trading.account.application.result;

import com.example.trading.account.domain.model.Account;

/**
 * Result — application output shared with other modules. Not the domain Account, not an HTTP DTO.
 */
public record AccountView(Long id, String type, String name) {

	public static AccountView from(Account account) {
		return new AccountView(account.getId(), account.getType().name(), account.getName());
	}
}
