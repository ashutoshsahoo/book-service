package com.ashu.practice.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfiguration {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, ClientConfigProperties clientConfigProperties) {
		// @formatter:off
		return builder
				.defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.rootUri(clientConfigProperties.getAuthServiceUrl())
				.setReadTimeout(Duration.ofSeconds(10))
				.build();
		// @formatter:on
	}
}
