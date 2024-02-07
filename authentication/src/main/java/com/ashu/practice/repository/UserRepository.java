package com.ashu.practice.repository;

import com.ashu.practice.model.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDao, Long> {

	Optional<UserDao> findByUsername(String username);

	Optional<UserDao> findByEmail(String email);

}
