package com.ashu.practice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "client")
@Data
public class ClientConfigProperties {

	private String authServiceUrl;
}
