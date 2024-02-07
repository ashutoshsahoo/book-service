package com.ashu.practice.repository;

import com.ashu.practice.model.Role;
import com.ashu.practice.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleType name);
}
