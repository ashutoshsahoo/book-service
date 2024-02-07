package com.ashu.practice.dto;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

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
