package com.ashu.practice.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashu.practice.dto.LoginRequest;
import com.ashu.practice.dto.LoginResponse;
import com.ashu.practice.dto.SignupRequest;
import com.ashu.practice.dto.ValidateTokenRequest;
import com.ashu.practice.dto.ValidateTokenResponse;
import com.ashu.practice.exception.InvalidTokenException;
import com.ashu.practice.model.UserDetailsImpl;
import com.ashu.practice.service.JwtTokenInternalService;
import com.ashu.practice.service.UserDetailsInternalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

	private final AuthenticationManager authManager;

	private final JwtTokenInternalService jwtTokenService;

	private final UserDetailsInternalService userDetailsService;

	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> createToken(@RequestBody @Valid LoginRequest request) {
		log.debug("signing in with username" + request.getUsername());
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return ResponseEntity.ok(jwtTokenService.generateToken(authentication));
	}

	@PostMapping(value = "/signup")
	public ResponseEntity<Void> saveUser(@RequestBody @Valid SignupRequest request) {
		log.debug("signing up with username" + request.getUsername());
		userDetailsService.save(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping(value = "/token")
	public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody @Valid ValidateTokenRequest request) {
		log.debug("validate token");
		String username = jwtTokenService.getUsernameFromToken(request.getToken())
				.orElseThrow(InvalidTokenException::new);
		return ResponseEntity.ok(new ValidateTokenResponse(username));
	}

	// TODO: Secure this end point
	@GetMapping(value = "/{username}")
	public ResponseEntity<UserDetailsImpl> getUserDetails(@PathVariable(name = "username") String username) {
		log.debug("getUserDetails for username=" + username);
		UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
		return ResponseEntity.ok(user);
	}

}
