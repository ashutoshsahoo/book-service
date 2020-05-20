package com.ashu.practice.dto;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {

	private static final long serialVersionUID = -2262958775856445575L;

	private String username;

	@JsonIgnore
	private String password;

	private String email;

	private Set<String> roles;
}
