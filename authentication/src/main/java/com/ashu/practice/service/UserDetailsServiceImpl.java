package com.ashu.practice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ashu.practice.dto.SignupRequest;
import com.ashu.practice.dto.UpdatePasswordRequest;
import com.ashu.practice.dto.UserDto;
import com.ashu.practice.dto.UserUpdateRequest;
import com.ashu.practice.exception.EmailAlreadyExistsException;
import com.ashu.practice.exception.EmailNotFoundException;
import com.ashu.practice.exception.RoleDoesNotExistException;
import com.ashu.practice.exception.UsernameAlreadyExistsException;
import com.ashu.practice.model.Role;
import com.ashu.practice.model.RoleType;
import com.ashu.practice.model.UserDao;
import com.ashu.practice.model.UserDetailsImpl;
import com.ashu.practice.repository.RoleRepository;
import com.ashu.practice.repository.UserRepository;
import com.ashu.practice.utils.CacheConstants;

@Service
public class UserDetailsServiceImpl implements UserDetailsInternalService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Cacheable(cacheNames = { CacheConstants.USER_DETAILS_CACHE }, key = "#username")
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) {
		UserDao userDao = findByUsername(username);
		return convertDaoToUserDetails(userDao);
	}

	@Override
	public UserDto save(SignupRequest request) {

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new UsernameAlreadyExistsException(request.getUsername());
		}

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException(request.getUsername());
		}

		Set<Role> roles = convertRole(request.getRoles());

		// Create new user's account
		UserDao user = new UserDao(request.getUsername(), request.getEmail(), encoder.encode(request.getPassword()),
				roles);
		return convertModelToDto(userRepository.save(user));
	}

	// @CachePut(cacheNames = { CacheConstants.USER_DAO_CACHE }, key = "#username")
	@Override
	public UserDto update(String username, UserUpdateRequest request) {
		UserDao userDao = findByUsername(username);
		String email = request.getEmail();
		Optional<UserDao> userDaoByEmail = userRepository.findByEmail(email);
		if (userDaoByEmail.isPresent() && !userDaoByEmail.get().getId().equals(userDao.getId())) {
			throw new EmailAlreadyExistsException(email);
		}
		Set<Role> roles = convertRole(request.getRoles());
		userDao.getRoles().clear();
		userDao.setRoles(roles);
		userDao.setEmail(email);
		userDao = userRepository.save(userDao);
		return convertModelToDto(userDao);
	}

	@Override
	public UserDto viewByUsername(String username) {
		UserDao userDao = findByUsername(username);
		return convertModelToDto(userDao);
	}

	// @Cacheable(cacheNames = { CacheConstants.USER_DAO_CACHE }, key = "#email")
	@Override
	public UserDto viewByEmail(String email) {
		UserDao userDao = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
		return convertModelToDto(userDao);
	}

	@Override
	public Page<UserDto> viewAll(Pageable pageable) {
		Page<UserDao> users = userRepository.findAll(pageable);
		return users.map(this::convertModelToDto);
	}

	@CacheEvict(cacheNames = { CacheConstants.USER_DETAILS_CACHE }, key = "#username")
	@Override
	public void delete(String username) {
		UserDao userDao = findByUsername(username);
		userRepository.delete(userDao);
	}

	@Override
	public void changePassword(String username, UpdatePasswordRequest request) {
		UserDao userDao = findByUsername(username);
		if (!encoder.matches(request.getOldPassword(), userDao.getPassword())) {
			throw new BadCredentialsException("Provided credentials are not correct");
		}
		userDao.setPassword(encoder.encode(request.getNewPassword()));
		userRepository.save(userDao);
	}

	private UserDto convertModelToDto(UserDao userDao) {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDao, userDto, "roles");
		Set<String> roles = userDao.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
		userDto.setRoles(roles);
		return userDto;
	}

	private UserDetailsImpl convertDaoToUserDetails(UserDao user) {

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		return new UserDetailsImpl(user.getUsername(), user.getEmail(), user.getPassword(), authorities);
	}

	private UserDao findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}

	private Set<Role> convertRole(Set<String> strRoles) {
		Set<Role> roles = new HashSet<>();

		// TODO: support for arbitrary roles
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(RoleType.ROLE_USER).orElseThrow(RoleDoesNotExistException::new);
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
							.orElseThrow(() -> new RoleDoesNotExistException(role));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(RoleType.ROLE_MODERATOR)
							.orElseThrow(() -> new RoleDoesNotExistException(role));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
							.orElseThrow(() -> new RoleDoesNotExistException(role));
					roles.add(userRole);
				}
			});
		}
		return roles;
	}

}
