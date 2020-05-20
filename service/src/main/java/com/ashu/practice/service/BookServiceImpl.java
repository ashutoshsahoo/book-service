package com.ashu.practice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.stereotype.Service;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.dto.BookSearchRequest;
import com.ashu.practice.exception.BookNotFoundException;
import com.ashu.practice.exception.IsbnAlreadyExistsException;
import com.ashu.practice.model.Book;
import com.ashu.practice.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepo;

	public BookServiceImpl(BookRepository bookRepo) {
		super();
		this.bookRepo = bookRepo;
	}

	@Override
	public BookDto create(BookDto bookDto) {
		Optional<Book> bookExist = bookRepo.findByIsbn(bookDto.getIsbn());
		if (bookExist.isPresent()) {
			throw new IsbnAlreadyExistsException(bookDto.getIsbn());
		}
		Book book = convertDtoToModel(bookDto);
		book = bookRepo.saveAndFlush(book);
		return convertModelToDto(book);
	}

	@Override
	public BookDto findById(Long id) {
		return convertModelToDto(findBookById(id));
	}

	@Override
	public List<BookDto> search(BookSearchRequest request) {
		Book book = new Book();
		book.setName(request.getName());
		book.setAuthor(request.getAuthor());
		book.setIsbn(request.getIsbn());
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("isbn", GenericPropertyMatcher::contains)
				.withMatcher("author", GenericPropertyMatcher::contains);
		Example<Book> example = Example.of(book, matcher);
		List<Book> books = bookRepo.findAll(example);
		return books.stream().map(this::convertModelToDto).collect(Collectors.toList());
	}

	@Override
	public List<BookDto> findAll() {
		List<Book> books = bookRepo.findAll();
		return books.stream().map(this::convertModelToDto).collect(Collectors.toList());
	}

	@Override
	public BookDto update(Long id, BookDto bookDto) {
		Book bookFound = findBookById(id);
		String isbn = bookDto.getIsbn();
		Optional<Book> bookWithIsbnFound = bookRepo.findByIsbn(isbn);
		if (bookWithIsbnFound.isPresent() && !bookWithIsbnFound.get().getId().equals(bookFound.getId())) {
			throw new IsbnAlreadyExistsException(isbn);
		}
		Book book = convertDtoToModel(bookDto);
		book.setId(bookFound.getId());
		book = bookRepo.saveAndFlush(book);
		return convertModelToDto(book);
	}

	@Override
	public void delete(Long id) {
		Book bookFound = findBookById(id);
		bookRepo.delete(bookFound);
	}

	private Book findBookById(Long id) {
		return bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException(id));
	}

	private BookDto convertModelToDto(Book book) {
		BookDto bookDto = new BookDto();
		BeanUtils.copyProperties(book, bookDto);
		return bookDto;
	}

	private Book convertDtoToModel(BookDto bookDto) {
		Book book = new Book();
		BeanUtils.copyProperties(bookDto, book);
		return book;
	}

}
