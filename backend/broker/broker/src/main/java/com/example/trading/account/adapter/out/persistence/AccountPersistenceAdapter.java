package com.example.trading.account.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.trading.account.application.port.out.AccountRepository;
import com.example.trading.account.domain.model.Account;
import com.example.trading.account.domain.model.AccountType;

@Repository
public class AccountPersistenceAdapter implements AccountRepository {

	private final SpringDataAccountRepository repository;

	AccountPersistenceAdapter(SpringDataAccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public Account save(Account account) {
		return AccountMapper.toDomain(repository.save(AccountMapper.toEntity(account)));
	}

	@Override
	public Optional<Account> findById(Long id) {
		return repository.findById(id).map(AccountMapper::toDomain);
	}

	@Override
	public List<Account> findByUserId(Long userId) {
		return repository.findByUserId(userId).stream().map(AccountMapper::toDomain).toList();
	}

	@Override
	public Optional<Account> findFirstByUserIdAndType(Long userId, AccountType type) {
		return repository.findFirstByUserIdAndTypeOrderByIdAsc(userId, type).map(AccountMapper::toDomain);
	}
}
