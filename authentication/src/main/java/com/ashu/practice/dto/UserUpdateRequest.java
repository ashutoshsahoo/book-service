package com.ashu.practice.dto;

import java.util.Set;

import lombok.Data;

@Data
public class UserUpdateRequest {

	private String email;

	private Set<String> roles;
}
