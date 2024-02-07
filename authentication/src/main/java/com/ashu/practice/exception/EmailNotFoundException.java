package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 6910482317634886181L;

	public EmailNotFoundException(String email) {
		super(email + " does not exist");
	}

}
