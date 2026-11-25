package com.example.trading.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.trading.user.application.port.out.UserRepository;
import com.example.trading.user.domain.model.User;

/**
 * Adapter out (persistence) — implements {@link UserRepository} with JPA.
 * Application services depend on the port, not on this class or UserJpaEntity.
 */
@Repository
public class UserPersistenceAdapter implements UserRepository {

	private final SpringDataUserRepository repository;

	UserPersistenceAdapter(SpringDataUserRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return repository.findByEmail(email).map(UserMapper::toDomain);
	}

	@Override
	public User save(User user) {
		return UserMapper.toDomain(repository.save(UserMapper.toEntity(user)));
	}
}
