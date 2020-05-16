package com.ashu.practice.exception;

public class IsbnAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -748233918205301977L;

	public IsbnAlreadyExistsException(String isbn) {
		super(String.format("The requested isbn %s already exists", isbn));
	}

}
