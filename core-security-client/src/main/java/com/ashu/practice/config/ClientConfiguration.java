package com.ashu.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfiguration {

	@Bean
	public RestClient restClient( ClientConfigProperties clientConfigProperties) {
		return RestClient.builder()
				// Enable request-response logging
				.requestFactory(new HttpComponentsClientHttpRequestFactory())
				//Globally Set Header - Once
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.baseUrl(clientConfigProperties.getAuthServiceUrl())
				.build();
	}

}
