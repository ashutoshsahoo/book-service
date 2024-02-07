package com.ashu.practice.service;

import com.ashu.practice.dto.SignupRequest;
import com.ashu.practice.dto.UpdatePasswordRequest;
import com.ashu.practice.dto.UserDto;
import com.ashu.practice.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsInternalService extends UserDetailsService {

	UserDto save(SignupRequest request);

	UserDto update(String username, UserUpdateRequest request);

	UserDto viewByUsername(String username);

	UserDto viewByEmail(String email);

	Page<UserDto> viewAll(Pageable request);

	void delete(String username);

	void changePassword(String username, UpdatePasswordRequest request);

}
