package com.ashu.practice.service;

import com.ashu.practice.config.JwtConfigProperties;
import com.ashu.practice.dto.LoginResponse;
import com.ashu.practice.model.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenInternalService {

    private final JwtConfigProperties jwtConfigProperties;

    @Override
    public Optional<String> getUsernameFromToken(String token) {
        String username = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes());
            username = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
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

    @Override
    public LoginResponse generateToken(Authentication authentication) {

        SecretKey jwtSecretKey = Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes());
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        ZonedDateTime creationTime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime expirationTime = ZonedDateTime.now(ZoneId.of("UTC"))
                .plus(Duration.ofMillis(jwtConfigProperties.getTokenValidity())).truncatedTo(ChronoUnit.SECONDS);
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
        // @formatter:off
		String token = Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(Date.from(creationTime.toInstant()))
				.header().add("type", jwtConfigProperties.getType())
                .and()
                .issuer(jwtConfigProperties.getIssuer())
				.audience().add(jwtConfigProperties.getAudience())
                .and()
				.expiration(Date.from(expirationTime.toInstant()))
				.signWith(jwtSecretKey, Jwts.SIG.HS512)
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
