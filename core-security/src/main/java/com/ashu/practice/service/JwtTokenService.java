package com.ashu.practice.service;

import java.util.Optional;

public interface JwtTokenService {

	/*
	 * Retrieve username from jwt token.
	 */
	Optional<String> getUsernameFromToken(String token);

	/*
	 * Validate token.
	 */
	// boolean validateToken(String token);

}
