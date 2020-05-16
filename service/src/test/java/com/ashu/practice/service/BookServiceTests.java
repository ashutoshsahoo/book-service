package com.ashu.practice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ashu.practice.dto.BookDto;
import com.ashu.practice.exception.BookNotFoundException;
import com.ashu.practice.model.Book;
import com.ashu.practice.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

	@Mock
	private BookRepository mockRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	@Test
	void create_OK() {
		Book mockBook = Book.builder().id(1L).isbn("9780596520687").name("test name").author("test author").build();
		when(mockRepository.saveAndFlush(any(Book.class))).thenReturn(mockBook);
		BookDto paload = BookDto.builder().isbn("9780596520687").name("test name").author("test author").build();
		BookDto bookResponse = bookService.create(paload);
		assertThat(bookResponse.getId()).isEqualTo(1L);
		verify(mockRepository, times(1)).saveAndFlush(mockBook);
	}

	@Test
	void findById_OK() throws Exception {
		Book mockBook = Book.builder().id(1L).isbn("0123456789867").name("test name").author("test author").build();
		when(mockRepository.findById(1L)).thenReturn(Optional.of(mockBook));
		BookDto bookResponse = bookService.findById(1L);
		assertThat(bookResponse).isNotNull();
		assertThat(bookResponse).isEqualTo(mockBook);
		verify(mockRepository, times(1)).findById(1L);
	}

	@Test
	void findById_NotFound() throws Exception {
		when(mockRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
		assertThrows(BookNotFoundException.class, () -> bookService.findById(1L));
	}

}
