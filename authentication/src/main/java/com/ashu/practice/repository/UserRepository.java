package com.ashu.practice.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ashu.practice.model.UserDao;
import com.ashu.practice.utils.CacheConstants;

public interface UserRepository extends JpaRepository<UserDao, Long> {

	@Cacheable(cacheNames = { CacheConstants.USERS_CACHE }, key = "#username")
	Optional<UserDao> findByUsername(String username);

	@CachePut(cacheNames = { CacheConstants.USERS_CACHE }, key = "#email")
	Optional<UserDao> findByEmail(String email);

	@CachePut(cacheNames = { CacheConstants.USERS_CACHE }, key = "#entity.username")
	@Override
	<S extends UserDao> S save(S entity);

	@CachePut(cacheNames = { CacheConstants.USERS_CACHE }, key = "#entity.username")
	@Override
	<S extends UserDao> S saveAndFlush(S entity);

	@CacheEvict(cacheNames = { CacheConstants.USERS_CACHE }, key = "#entity.username")
	@Override
	void delete(UserDao entity);

}
