package com.ashu.practice.service;

import java.util.List;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.dto.BookSearchRequest;

public interface BookService {

	BookDto create(BookDto bookDto);

	BookDto findById(Long id);

	List<BookDto> search(BookSearchRequest request);

	List<BookDto> findAll();

	BookDto update(Long id, BookDto bookDto);

	void delete(Long id);
}
