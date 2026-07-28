package com.ashu.practice;

import com.ashu.practice.model.Book;
import com.ashu.practice.model.Book.BookBuilder;
import com.ashu.practice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BookServiceApplicationTests {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookRepository mockRepository;

    @BeforeEach
    void init() {
        Book book = Book.builder().id(1L).isbn("9780596520687").name("test name").author("test author").build();
        when(mockRepository.findById(1L)).thenReturn(Optional.of(book));
    }

    @Test
    void find_noAuth_401() {
        mockMvc.perform(get("/books/{id}", 1))
                .assertThat().hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @WithMockUser("USER")
    void findById_OK() {
        // @formatter:off
		mockMvc
		.get().uri("/books/1")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
				.assertThat()
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$.id", id -> id.assertThat().isEqualTo(1))
				.hasPathSatisfying("$.name", name -> name.assertThat().isEqualTo("test name"))
				.hasPathSatisfying("$.auther", author -> author.assertThat().isEqualTo("test author"))
				.hasPathSatisfying("$.isbn", isbn -> isbn.assertThat().isEqualTo("9780596520687"));
		// @formatter:on
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser("USER")
    void find_bookIdNotFound_404() {
        // @formatter:off
		mockMvc.perform(get("/books/{id}", 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.assertThat().hasStatus(HttpStatus.NOT_FOUND);
		// @formatter:on
    }

    @Test
    @WithMockUser("USER")
    void find_allBooks_OK() {
        Book book1 = Book.builder().id(1L).isbn("9780596520687").name("test name").author("test author").build();
        Book book2 = Book.builder().id(2L).isbn("9780596520688").name("test name two").author("test author two")
                .build();
        List<Book> books = Arrays.asList(book1, book2);

        when(mockRepository.findAll()).thenReturn(books);
        // @formatter:off
		mockMvc.perform(get("/books")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.assertThat()
				.hasContentType(MediaType.APPLICATION_JSON)
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$", size -> size.assertThat().isEqualTo(books.size()))
				.hasPathSatisfying("$[0].id", id -> id.assertThat().isEqualTo(1));

//				.andExpect(jsonPath("$[0].name", is("test name")))
//				.andExpect(jsonPath("$[0].author", is("test author")))
//				.andExpect(jsonPath("$[0].isbn", is("9780596520687")))
//				.andExpect(jsonPath("$[1].id", is(2)))
//				.andExpect(jsonPath("$[1].name", is("test name two")))
//				.andExpect(jsonPath("$[1].author", is("test author two")))
//				.andExpect(jsonPath("$[1].isbn", is("9780596520688")));
		// @formatter:on
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser("USER")
    void save_OK() {
        BookBuilder bookBuilder = Book.builder().isbn("9780596520687").name("test name").author("test author");
        when(mockRepository.saveAndFlush(any(Book.class))).thenReturn(bookBuilder.id(1L).build());
        // @formatter:off
		mockMvc.post().uri("/books")
				.content(objectMapper.writeValueAsString(bookBuilder.build()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.assertThat()
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$.id", id -> id.assertThat().isEqualTo(1))
				.hasPathSatisfying("$.name", name -> name.assertThat().isEqualTo("test name"))
				.hasPathSatisfying("$.auther", author -> author.assertThat().isEqualTo("test author"))
				.hasPathSatisfying("$.isbn", isbn -> isbn.assertThat().isEqualTo("9780596520687"));
		// @formatter:on
        verify(mockRepository, times(1)).saveAndFlush(any(Book.class));
    }

//    @Test
//    @WithMockUser("USER")
//    void save_emptyName_nullIsbn_400() throws Exception {
//        Book book = Book.builder().isbn(null).name("").author("test author").build();
//        // @formatter:off
//		mockMvc.perform(post("/books")
//				.content(objectMapper.writeValueAsString(book))
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON))
//				// .andDo(print())
//				.andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.message", is("Validation Failed")))
//				.andExpect(jsonPath("$.details").isArray())
//				.andExpect(jsonPath("$.details", hasSize(4)))
//				.andExpect(jsonPath("$.details", hasItem("name should not be empty or null")))
//				.andExpect(jsonPath("$.details", hasItem("name should have minimun 2 characters and maximum 30 characters length")))
//				.andExpect(jsonPath("$.details", hasItem("Invalid name - no special characters or numbers allowed")))
//				.andExpect(jsonPath("$.details", hasItem("isbn should not be empty or null")));
//		// @formatter:on
//        verify(mockRepository, times(0)).saveAndFlush(any(Book.class));
//    }

    @Test
    @WithMockUser("USER")
    void update_book_OK() {
        BookBuilder bookBuilder = Book.builder().isbn("9780596520687").name("test name updated").author("test author");
        when(mockRepository.saveAndFlush(any(Book.class))).thenReturn(bookBuilder.id(1L).build());
        // @formatter:off
		mockMvc.perform(put("/books/{id}", 1)
				.content(objectMapper.writeValueAsString(bookBuilder.build()))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.assertThat()
				.hasStatusOk()
				.bodyJson()
				.hasPathSatisfying("$.id", id -> id.assertThat().isEqualTo(1))
				.hasPathSatisfying("$.name", name -> name.assertThat().isEqualTo("test name"))
				.hasPathSatisfying("$.auther", author -> author.assertThat().isEqualTo("test author"))
				.hasPathSatisfying("$.isbn", isbn -> isbn.assertThat().isEqualTo("9780596520687"));
		// @formatter:on
    }

    @Test
    @WithMockUser("USER")
    void delete_employee_OK() {
        doNothing().when(mockRepository).delete(any(Book.class));
        // @formatter:off
		mockMvc.perform(delete("/books/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.assertThat()
				.hasStatusOk();
		// @formatter:on
        verify(mockRepository, times(1)).delete(any(Book.class));
    }

    @Test
    @WithMockUser("USER")
    void delete_employee_notFound_404() {
        doNothing().when(mockRepository).delete(any(Book.class));
        when(mockRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
        // @formatter:off
		mockMvc.perform(delete("/books/{id}", 2)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.assertThat()
				.hasStatus(HttpStatus.NOT_FOUND);
		// @formatter:on
        verify(mockRepository, times(0)).delete(any(Book.class));
        verify(mockRepository, times(1)).findById(2L);
    }

}
