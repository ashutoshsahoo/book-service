package com.ashu.practice;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ashu.practice.model.Book;
import com.ashu.practice.model.Book.BookBuilder;
import com.ashu.practice.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BookRepository mockRepository;

	@BeforeEach
	public void init() {
		Book book = Book.builder().id(1L).isbn("9780596520687").name("test name").author("test author").build();
		when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
	}

	@Test
	public void findById_OK() throws Exception {
		// @formatter:off
		mockMvc
		.perform(get("/books/{id}", 1)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON))
		// .andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.name", is("test name")))
		.andExpect(jsonPath("$.author", is("test author")))
		.andExpect(jsonPath("$.isbn", is("9780596520687")));
		// @formatter:on
		verify(mockRepository, times(1)).findById(1L);
	}

	@Test
	public void find_bookIdNotFound_404() throws Exception {
		// @formatter:off
		mockMvc.perform(get("/books/{id}", 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		// @formatter:on
	}

	@Test
	public void find_allBooks_OK() throws Exception {
		Book book1 = Book.builder().id(1L).isbn("9780596520687").name("test name").author("test author").build();
		Book book2 = Book.builder().id(2L).isbn("9780596520688").name("test name two").author("test author two")
				.build();
		List<Book> books = Arrays.asList(book1, book2);

		when(mockRepository.findAll()).thenReturn(books);
		// @formatter:off
		mockMvc.perform(get("/books")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("test name")))
				.andExpect(jsonPath("$[0].author", is("test author")))
				.andExpect(jsonPath("$[0].isbn", is("9780596520687")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("test name two")))
				.andExpect(jsonPath("$[1].author", is("test author two")))
				.andExpect(jsonPath("$[1].isbn", is("9780596520688")));
		// @formatter:on
		verify(mockRepository, times(1)).findAll();
	}

	@Test
	public void save_OK() throws Exception {
		BookBuilder bookBuilder = Book.builder().isbn("9780596520687").name("test name").author("test author");
		when(mockRepository.saveAndFlush(any(Book.class))).thenReturn(bookBuilder.id(1L).build());
		// @formatter:off
		mockMvc.perform(post("/books")
				.content(objectMapper.writeValueAsString(bookBuilder.build()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("test name")))
				.andExpect(jsonPath("$.author", is("test author")))
				.andExpect(jsonPath("$.isbn", is("9780596520687")));
		// @formatter:on
		verify(mockRepository, times(1)).saveAndFlush(any(Book.class));
	}

	@Test
	public void save_emptyName_nullIsbn_400() throws Exception {
		Book book = Book.builder().isbn(null).name("").author("test author").build();
		// @formatter:off
		mockMvc.perform(post("/books")
				.content(objectMapper.writeValueAsString(book))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Validation Failed")))
				.andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasSize(4)))
				.andExpect(jsonPath("$.details", hasItem("name should not be empty or null")))
				.andExpect(jsonPath("$.details", hasItem("name should have minimun 2 characters and maximum 30 characters length")))
				.andExpect(jsonPath("$.details", hasItem("Invalid name - no special characters allowed")))
				.andExpect(jsonPath("$.details", hasItem("isbn should not be empty or null")));
		// @formatter:on
		verify(mockRepository, times(0)).saveAndFlush(any(Book.class));
	}

	@Test
	public void update_book_OK() throws Exception {
		BookBuilder bookBuilder = Book.builder().isbn("9780596520687").name("test name updated").author("test author");
		when(mockRepository.saveAndFlush(any(Book.class))).thenReturn(bookBuilder.id(1L).build());
		// @formatter:off
		mockMvc.perform(put("/books/{id}", 1)
				.content(objectMapper.writeValueAsString(bookBuilder.build()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("test name updated")))
				.andExpect(jsonPath("$.author", is("test author")))
				.andExpect(jsonPath("$.isbn", is("9780596520687")));
		// @formatter:on
	}

	@Test
	public void delete_employee_OK() throws Exception {
		doNothing().when(mockRepository).delete(any(Book.class));
		// @formatter:off
		mockMvc.perform(delete("/books/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isOk());
		// @formatter:on
		verify(mockRepository, times(1)).delete(any(Book.class));
	}

	@Test
	public void delete_employee_notFound_404() throws Exception {
		doNothing().when(mockRepository).delete(any(Book.class));
		when(mockRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
		// @formatter:off
		mockMvc.perform(delete("/books/{id}", 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isNotFound());
		// @formatter:on
		verify(mockRepository, times(0)).delete(any(Book.class));
		verify(mockRepository, times(1)).findById(2L);
	}

}
