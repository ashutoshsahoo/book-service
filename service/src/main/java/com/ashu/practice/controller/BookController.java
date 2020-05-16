package com.ashu.practice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.dto.BookSearchRequest;
import com.ashu.practice.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BookController {

	private final BookService bookService;

	public BookController(final BookService bookService) {
		super();
		this.bookService = bookService;
	}

	@PostMapping
	public BookDto create(@RequestBody @Valid BookDto bookDto) {
		log.debug("Request received for book creation", bookDto.toString());
		return bookService.create(bookDto);
	}

	@GetMapping
	public List<BookDto> findAll() {
		log.debug("Request received for listAll books");
		return bookService.findAll();
	}

	@GetMapping(path = "/{id}")
	public BookDto findById(@PathVariable(name = "id") Long id) {
		log.debug("Request received for findById for id=" + id);
		return bookService.findById(id);
	}

	@GetMapping(path = "/search")
	public List<BookDto> search(@Valid BookSearchRequest request) {
		log.debug("Request received for search=" + request);
		return bookService.search(request);
	}

	@PutMapping(path = "/{id}")
	public BookDto update(@PathVariable(name = "id") Long id, @RequestBody @Valid BookDto bookDto) {
		log.debug("Request received for book updation with id=" + id, bookDto.toString());
		return bookService.update(id, bookDto);
	}

	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable(name = "id") Long id) {
		log.debug("Request received for book deletion with id=" + id);
		bookService.delete(id);
	}

}
