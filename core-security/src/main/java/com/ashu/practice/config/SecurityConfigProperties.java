package com.ashu.practice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Data
@NoArgsConstructor
public class SecurityConfigProperties {

	private Paths paths;

	@Data
	@NoArgsConstructor
	@ConfigurationProperties(prefix = "paths")
	public static class Paths {
		/*
		 * List of allowed/permitted paths.
		 */
		private List<String> permitted;

		/*
		 * List of ignored paths.
		 */
		private List<String> ignored;
	}
}
