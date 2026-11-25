package com.example.trading.user.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {

	boolean existsByEmail(String email);

	Optional<UserJpaEntity> findByEmail(String email);
}
