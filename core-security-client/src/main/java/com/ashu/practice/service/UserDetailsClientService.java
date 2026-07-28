package com.ashu.practice.service;

import com.ashu.practice.model.UserDetailsImpl;
import com.ashu.practice.utils.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserDetailsClientService implements UserDetailsService {

    private final RestClient restClient;

    @Cacheable(cacheNames = {CacheConstants.USERS_CACHE}, key = "#username")
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) {
        return getUserDetails(username);
    }

    private UserDetailsImpl getUserDetails(String username) {
        UserDetailsImpl response = restClient.get().uri(String.format("/%s", username))
                .retrieve().body(UserDetailsImpl.class);
        if (response == null) {
            throw new UsernameNotFoundException("Requested username not found");
        }

        return response;
    }

}
