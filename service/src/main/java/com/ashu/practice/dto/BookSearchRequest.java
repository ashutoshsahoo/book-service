package com.ashu.practice.dto;

import com.ashu.practice.util.BookConstants;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookSearchRequest {

	@Pattern(regexp = BookConstants.ISBN_REGEX, message = "Invalid isbn query")
	private String isbn;

	@Pattern(regexp = BookConstants.NAME_REGEX, message = "Invalid name query - no special characters allowed")
	private String name;

	@Pattern(regexp = BookConstants.NAME_REGEX, message = BookConstants.NAME_REGEX_FAILURE_MSG)
	private String author;

}
