package com.ashu.practice.service;

import org.springframework.security.core.Authentication;

import com.ashu.practice.dto.LoginResponse;

public interface JwtTokenInternalService extends JwtTokenService {

	LoginResponse generateToken(Authentication authentication);
}
