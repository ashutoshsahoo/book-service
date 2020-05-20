package com.ashu.practice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashu.practice.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByName(String name);

	Optional<Book> findByIsbn(String isbn);

}
