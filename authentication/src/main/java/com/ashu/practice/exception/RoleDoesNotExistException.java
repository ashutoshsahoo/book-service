package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RoleDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = -492557811186895524L;

	public RoleDoesNotExistException(String role) {
		super("Error: No matching role found for " + role);
	}

	public RoleDoesNotExistException() {
		super("Error: No matching role found");
	}

}
