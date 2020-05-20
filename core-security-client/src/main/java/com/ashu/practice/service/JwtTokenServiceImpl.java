package com.ashu.practice.service;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ashu.practice.dto.ValidateTokenRequest;
import com.ashu.practice.dto.ValidateTokenResponse;
import com.ashu.practice.utils.CacheConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

	private final RestTemplate restTemplate;

	@Cacheable(cacheNames = { CacheConstants.TOKEN_CACHE }, key = "#token")
	@Override
	public Optional<String> getUsernameFromToken(String token) {
		String username = getUserFromToken(token).orElseThrow(() -> new BadCredentialsException("Invalid token"));
		return Optional.ofNullable(username);
	}

	private Optional<String> getUserFromToken(String token) {
		ValidateTokenRequest payload = new ValidateTokenRequest(token);
		ResponseEntity<ValidateTokenResponse> response = restTemplate.postForEntity("/token", payload,
				ValidateTokenResponse.class);
		return Optional.ofNullable(response.getBody().getUsername());
	}
}
