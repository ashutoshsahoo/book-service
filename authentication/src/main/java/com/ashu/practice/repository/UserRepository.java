package com.ashu.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashu.practice.model.UserDao;

public interface UserRepository extends JpaRepository<UserDao, Long> {

	Optional<UserDao> findByUsername(String username);

	Optional<UserDao> findByEmail(String email);

}
