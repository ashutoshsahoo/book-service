package com.ashu.practice.config;

import com.ashu.practice.service.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityBeanConfig {

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(UserDetailsService userDetailsService,
                                                               JwtTokenService tokenService) {
        return new AuthenticationTokenFilter(userDetailsService, tokenService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
