package com.ashu.practice.controller;

import com.ashu.practice.dto.UpdatePasswordRequest;
import com.ashu.practice.dto.UserDto;
import com.ashu.practice.dto.UserUpdateRequest;
import com.ashu.practice.service.UserDetailsInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsInternalService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> viewAll(Pageable pageable) {
        log.debug("User viewAll request received");
        return userService.viewAll(pageable);
    }

    @GetMapping(path = "/{username}")
    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    public UserDto viewByUsername(@PathVariable(name = "username") String username) {
        log.debug("User viewByUsername request received for user={}", username);
        return userService.viewByUsername(username);
    }

    @GetMapping(path = "/email/{email}")
    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    public UserDto viewByEmail(@PathVariable(name = "email") String email) {
        return userService.viewByEmail(email);
    }

    @PutMapping(path = "/{username}")
    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    public UserDto update(@PathVariable(name = "username") String username,
                          @RequestBody @Valid UserUpdateRequest request) {
        log.debug("Update user request received for user={}", username);
        return userService.update(username, request);
    }

    @PatchMapping(path = "/{username}")
    @PreAuthorize("#username == principal.username")
    public void changePassword(@PathVariable(name = "username") String username,
                               @RequestBody @Valid UpdatePasswordRequest request) {
        log.debug("Change password request received for user={}", username);
        userService.changePassword(username, request);
    }

    @DeleteMapping(path = "/{username}")
    @PreAuthorize("#username == principal.username or hasRole('ADMIN')")
    public void delete(@PathVariable(name = "username") String username) {
        log.debug("Delete user request received for user={}", username);
        userService.delete(username);
    }

}
