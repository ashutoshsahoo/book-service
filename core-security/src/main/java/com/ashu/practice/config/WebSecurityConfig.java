package com.ashu.practice.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableCaching
public class WebSecurityConfig {

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Filter authenticationTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        List<String> permittedPathsList = securityConfigProperties.getPaths() != null
                ? securityConfigProperties.getPaths().getPermitted()
                : Collections.emptyList();
        String permittedPaths = convertPathsToString(permittedPathsList);
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers(permittedPaths).permitAll());

        // @formatter:off
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(auth ->
                        auth.requestMatchers(EndpointRequest.to("health"))
                                .permitAll()
                .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(handler-> handler.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on

        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        List<String> ignoredPathsList = securityConfigProperties.getPaths() != null
                ? securityConfigProperties.getPaths().getIgnored()
                : Collections.emptyList();
        String ignoredPaths = convertPathsToString(ignoredPathsList);
        return web -> web.ignoring().requestMatchers(ignoredPaths);
    }

    private String convertPathsToString(List<String> paths) {
        return StringUtils.collectionToDelimitedString(paths, ",");
    }
}
