package com.ashu.practice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {

	private static final long serialVersionUID = -2262958775856445575L;

	private String username;

	private String email;

	private Set<String> roles;
}
