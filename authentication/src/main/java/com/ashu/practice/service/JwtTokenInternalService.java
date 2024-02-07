package com.ashu.practice.service;

import com.ashu.practice.dto.LoginResponse;
import org.springframework.security.core.Authentication;

public interface JwtTokenInternalService extends JwtTokenService {

	LoginResponse generateToken(Authentication authentication);
}
