package com.ashu.practice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPointImpl authenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SecurityConfigProperties securityConfigProperties;

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilter() {
		return new AuthenticationTokenFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		List<String> permittedPathsList = securityConfigProperties.getPaths() != null
				? securityConfigProperties.getPaths().getPermitted()
				: null;
		if (permittedPathsList != null && !permittedPathsList.isEmpty()) {
			String permittedPaths = convertPathsToString(permittedPathsList);
			httpSecurity.authorizeRequests().antMatchers(permittedPaths).permitAll();
		}

		// @formatter:off
		httpSecurity.csrf().disable()
		.authorizeRequests()
		.requestMatchers(EndpointRequest.to("health")).permitAll()
		.anyRequest().authenticated().and()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// @formatter:on

		httpSecurity.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		List<String> ignoredPathsList = securityConfigProperties.getPaths() != null
				? securityConfigProperties.getPaths().getIgnored()
				: null;
		if (ignoredPathsList != null && !ignoredPathsList.isEmpty()) {
			String ignoredPaths = convertPathsToString(ignoredPathsList);
			web.ignoring().antMatchers(ignoredPaths);
		}
	}

	private String convertPathsToString(List<String> paths) {
		return StringUtils.collectionToDelimitedString(paths, ",");
	}
}
