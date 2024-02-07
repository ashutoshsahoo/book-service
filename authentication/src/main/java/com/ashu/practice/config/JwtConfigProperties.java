package com.ashu.practice.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "jwt")
@Data
@NoArgsConstructor
public class JwtConfigProperties {

	@NotBlank
	private String secret;

	private String issuer;

	private String type = "JWT";

	private String audience;

	private long tokenValidity = 1800_000; // 30*60*1000 = 30minutes
}
