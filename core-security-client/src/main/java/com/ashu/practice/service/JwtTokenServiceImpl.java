package com.ashu.practice.service;

import com.ashu.practice.dto.ValidateTokenRequest;
import com.ashu.practice.dto.ValidateTokenResponse;
import com.ashu.practice.utils.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final RestClient restClient;

    @Cacheable(cacheNames = {CacheConstants.TOKEN_CACHE}, key = "#token")
    @Override
    public Optional<String> getUsernameFromToken(String token) {
        return Optional.of(getUserFromToken(token));
    }

    private String getUserFromToken(String token) {
        ValidateTokenRequest payload = new ValidateTokenRequest(token);
        ValidateTokenResponse response = restClient.post().uri("/token").body(payload)
                .retrieve().body(ValidateTokenResponse.class);
        if (response != null) {
            return response.getUsername();
        }
        throw new BadCredentialsException("Invalid token");
    }
}
