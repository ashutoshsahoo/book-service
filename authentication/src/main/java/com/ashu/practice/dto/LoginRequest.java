package com.ashu.practice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {


	@NotBlank
	private String username;
	
	@NotBlank
	private String password;

}
