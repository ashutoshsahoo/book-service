package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 5387706583193625523L;

	public UsernameAlreadyExistsException(String username) {
		super("Username " + username + " already exists");
	}

}
