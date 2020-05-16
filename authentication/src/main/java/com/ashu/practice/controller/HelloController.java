package com.ashu.practice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class HelloController {

	@GetMapping("/all")
	public ResponseEntity<String> allAccess() {
		return new ResponseEntity<>("Public Content.", HttpStatus.OK);
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<String> userAccess() {
		return new ResponseEntity<>("User Content.", HttpStatus.OK);
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public ResponseEntity<String> moderatorAccess() {
		return new ResponseEntity<>("Moderator Board.", HttpStatus.OK);
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> adminAccess() {
		return new ResponseEntity<>("Admin Board.", HttpStatus.OK);
	}

}