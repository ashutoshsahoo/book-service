package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 7561433038399030451L;

	public InvalidTokenException() {
		super("Invalid token");
	}

}
