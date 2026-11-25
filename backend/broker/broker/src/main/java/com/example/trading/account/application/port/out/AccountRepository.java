package com.example.trading.account.application.port.out;

import java.util.List;
import java.util.Optional;

import com.example.trading.account.domain.model.Account;
import com.example.trading.account.domain.model.AccountType;

public interface AccountRepository {

	Account save(Account account);

	Optional<Account> findById(Long id);

	List<Account> findByUserId(Long userId);

	Optional<Account> findFirstByUserIdAndType(Long userId, AccountType type);
}
