package com.ashu.practice.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfigProperties {

	@NotBlank
	private String secret;

	private String issuer;

	private String type = "JWT";

	private String audience;

	private long tokenValidity = 1800_000; // 30*60*1000 = 30minutes
}
