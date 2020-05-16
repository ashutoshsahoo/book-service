package com.ashu.practice.service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.ashu.practice.config.JwtConfigProperties;
import com.ashu.practice.dto.LoginResponse;
import com.ashu.practice.model.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenInternalService {

	private final JwtConfigProperties jwtConfigProperties;

	public JwtTokenServiceImpl(JwtConfigProperties jwtConfigProperties) {
		super();
		this.jwtConfigProperties = jwtConfigProperties;
	}

	@Override
	public Optional<String> getUsernameFromToken(String token) {
		String username = null;
		try {
			username = Jwts.parserBuilder().setSigningKey(jwtConfigProperties.getSecret().getBytes()).build()
					.parseClaimsJws(token).getBody().getSubject();
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return Optional.ofNullable(username);
	}

//	@Override
//	public boolean validateToken(String token) {
//		try {
//			Jwts.parserBuilder().setSigningKey(jwtConfigProperties.getSecret().getBytes()).build()
//					.parseClaimsJws(token);
//			return true;
//		} catch (SignatureException e) {
//			log.error("Invalid JWT signature: {}", e.getMessage());
//		} catch (MalformedJwtException e) {
//			log.error("Invalid JWT token: {}", e.getMessage());
//		} catch (ExpiredJwtException e) {
//			log.error("JWT token is expired: {}", e.getMessage());
//		} catch (UnsupportedJwtException e) {
//			log.error("JWT token is unsupported: {}", e.getMessage());
//		} catch (IllegalArgumentException e) {
//			log.error("JWT claims string is empty: {}", e.getMessage());
//		}
//		return false;
//	}

	@Override
	public LoginResponse generateToken(Authentication authentication) {

		SecretKey jwtSecretKey = Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes());
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		ZonedDateTime creationTime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
		ZonedDateTime expirationTime = ZonedDateTime.now(ZoneId.of("UTC"))
				.plus(Duration.ofMillis(jwtConfigProperties.getTokenValidity())).truncatedTo(ChronoUnit.SECONDS);
		List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		// @formatter:off
		String token = Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				.setIssuedAt(Date.from(creationTime.toInstant()))
				.setHeaderParam("type", jwtConfigProperties.getType())
				.setIssuer(jwtConfigProperties.getIssuer())
				.setAudience(jwtConfigProperties.getAudience())
				.setExpiration(Date.from(expirationTime.toInstant()))
				.signWith(jwtSecretKey, SignatureAlgorithm.HS512)
				.compact();
		return LoginResponse.builder()
				.token(token)
				.type(jwtConfigProperties.getType())
				.username(userPrincipal.getUsername())
				.roles(roles)
				.creationTime(creationTime)
				.expirationTime(expirationTime)
				.build();
		// @formatter:on

	}

}
