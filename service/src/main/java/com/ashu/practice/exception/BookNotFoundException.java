package com.ashu.practice.exception;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4571451418690379635L;

	public BookNotFoundException(Long id) {
		super("Book not found for id=" + id);
	}
}
