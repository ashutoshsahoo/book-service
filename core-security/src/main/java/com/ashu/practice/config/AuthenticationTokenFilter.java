package com.ashu.practice.config;

import com.ashu.practice.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;

	private final JwtTokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			Optional<String> jwtIsPresent = parseJwt(request);
			Optional<String> usernameIsPresent;
			if (jwtIsPresent.isPresent()
					&& (usernameIsPresent = tokenService.getUsernameFromToken(jwtIsPresent.get())).isPresent()) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(usernameIsPresent.get());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private Optional<String> parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		String token = null;
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			token = headerAuth.substring(7);
		}
		return Optional.ofNullable(token);
	}
}
