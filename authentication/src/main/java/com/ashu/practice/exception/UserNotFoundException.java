package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 7581195708765089631L;

	public UserNotFoundException(String username) {
		super(username + " not found");
	}

}
