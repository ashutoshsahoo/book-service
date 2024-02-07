package com.ashu.practice.exception;

import java.io.Serial;

public class BookNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 4571451418690379635L;

	public BookNotFoundException(Long id) {
		super("Book not found for id=" + id);
	}
}
