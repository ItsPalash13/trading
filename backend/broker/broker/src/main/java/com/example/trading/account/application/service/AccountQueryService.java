package com.example.trading.account.application.service;

import org.springframework.stereotype.Service;

import com.example.trading.account.application.port.in.AccountQuery;
import com.example.trading.account.application.port.out.AccountRepository;
import com.example.trading.account.application.result.AccountView;
import com.example.trading.account.domain.model.Account;
import com.example.trading.account.domain.model.AccountType;
import com.example.trading.platform.exception.NotFoundException;

@Service
public class AccountQueryService implements AccountQuery {

	private final AccountRepository accountRepository;

	public AccountQueryService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public AccountView getDefaultAccount(Long userId) {
		return accountRepository.findFirstByUserIdAndType(userId, AccountType.REGULAR)
				.or(() -> accountRepository.findByUserId(userId).stream().findFirst())
				.map(AccountView::from)
				.orElseThrow(() -> new NotFoundException("No account found for user"));
	}

	@Override
	public AccountView getOwnedAccount(Long accountId, Long userId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new NotFoundException("Account not found"));
		account.ensureOwnedBy(userId);
		return AccountView.from(account);
	}

	/*
	 * Expected account switching (not wired yet).
	 * A web adapter in this module verifies ownership, then stores the selection:
	 *
	 * AccountView account = accountQuery.getOwnedAccount(accountId, userId);
	 * currentAccountSession.set(session, account.id());
	 */
}
