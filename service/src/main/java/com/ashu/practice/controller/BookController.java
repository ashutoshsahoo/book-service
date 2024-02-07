package com.ashu.practice.controller;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.dto.BookSearchRequest;
import com.ashu.practice.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/books",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid BookDto bookDto) {
        log.debug("Request received for book creation={}", bookDto);
        return bookService.create(bookDto);
    }

    @GetMapping
    public List<BookDto> findAll() {
        log.debug("Request received for listAll books");
        return bookService.findAll();
    }

    @GetMapping(path = "/{id}")
    public BookDto findById(@PathVariable(name = "id") Long id) {
        log.debug("Request received for findById for id={}", id);
        return bookService.findById(id);
    }

    @GetMapping(path = "/search")
    public List<BookDto> search(@Valid BookSearchRequest request) {
        log.debug("Request received for search={}", request);
        return bookService.search(request);
    }

    @PutMapping(path = "/{id}")
    public BookDto update(@PathVariable(name = "id") Long id, @RequestBody @Valid BookDto bookDto) {
        log.debug("Update request received for id={} and book={}", id, bookDto);
        return bookService.update(id, bookDto);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        log.debug("Request received for book deletion with id={}", id);
        bookService.delete(id);
    }

}
