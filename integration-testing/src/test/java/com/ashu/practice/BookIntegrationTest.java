package com.ashu.practice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.web.servlet.MockMvc;

@IfProfileValue(name = "spring.profiles.active", value = "integration-test")
@SpringBootTest
@AutoConfigureMockMvc
@Profile("integration-test")
public class BookIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void find_allBook_OK() throws Exception {

		// @formatter:off
				mockMvc.perform(get("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(1000)))
				.andExpect(jsonPath("$[0].name", is("Physics Book")))
				.andExpect(jsonPath("$[0].author", is("Ashutosh Sahoo")))
				.andExpect(jsonPath("$[0].isbn", is("9780596520688")));
		// @formatter:on
	}

}
