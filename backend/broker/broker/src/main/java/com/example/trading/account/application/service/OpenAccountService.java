package com.example.trading.account.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.trading.account.application.port.in.OpenAccountUseCase;
import com.example.trading.account.application.port.out.AccountRepository;
import com.example.trading.account.application.result.AccountView;
import com.example.trading.account.domain.model.Account;

@Service
public class OpenAccountService implements OpenAccountUseCase {

	private final AccountRepository accountRepository;

	public OpenAccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public AccountView openRegularAccount(Long userId) {
		return AccountView.from(accountRepository.save(Account.openRegular(userId)));
	}
}
