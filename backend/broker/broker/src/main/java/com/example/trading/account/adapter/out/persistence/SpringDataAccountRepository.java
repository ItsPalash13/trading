package com.example.trading.account.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading.account.domain.model.AccountType;

interface SpringDataAccountRepository extends JpaRepository<AccountJpaEntity, Long> {

	List<AccountJpaEntity> findByUserId(Long userId);

	Optional<AccountJpaEntity> findFirstByUserIdAndTypeOrderByIdAsc(Long userId, AccountType type);
}
