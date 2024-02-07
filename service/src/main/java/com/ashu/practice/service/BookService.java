package com.ashu.practice.service;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.dto.BookSearchRequest;

import java.util.List;

public interface BookService {

	BookDto create(BookDto bookDto);

	BookDto findById(Long id);

	List<BookDto> search(BookSearchRequest request);

	List<BookDto> findAll();

	BookDto update(Long id, BookDto bookDto);

	void delete(Long id);
}
