package com.ashu.practice.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

	private String token;

	private String type;

	private String username;

	private List<String> roles;
	
	private ZonedDateTime creationTime;
	
	private ZonedDateTime expirationTime;

	

}
