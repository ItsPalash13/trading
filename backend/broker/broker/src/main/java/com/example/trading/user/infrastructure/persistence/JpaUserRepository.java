package com.example.trading.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading.user.domain.model.User;
import com.example.trading.user.domain.repository.UserRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
