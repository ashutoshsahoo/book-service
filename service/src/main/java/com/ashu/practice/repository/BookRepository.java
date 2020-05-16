package com.ashu.practice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ashu.practice.model.Book;
import com.ashu.practice.utils.CacheConstants;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Cacheable(cacheNames = { CacheConstants.BOOKS_CACHE }, key = "#name")
	List<Book> findByName(String name);

	@Cacheable(cacheNames = { CacheConstants.BOOKS_CACHE }, key = "#isbn")
	Optional<Book> findByIsbn(String isbn);

}
