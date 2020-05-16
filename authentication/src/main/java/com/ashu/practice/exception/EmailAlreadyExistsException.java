package com.ashu.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -330535609084407762L;

	public EmailAlreadyExistsException(String email) {
		super(email + " already linked to another user");
	}

}
