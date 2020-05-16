package com.ashu.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashu.practice.model.Role;
import com.ashu.practice.model.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleType name);
}
