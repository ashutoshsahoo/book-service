package com.ashu.practice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserUpdateRequest {

	private String email;

	private Set<String> roles;
}
