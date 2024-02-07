package com.ashu.practice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USERS", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class UserDao implements Serializable {

	@Serial
	private static final long serialVersionUID = 2648537870798922434L;

	private static final String USER_ID_SEQUENCE = "USER_ID_SEQUENCE";

	@SequenceGenerator(name = USER_ID_SEQUENCE, sequenceName = USER_ID_SEQUENCE, allocationSize = 1, initialValue = 1000)
	@GeneratedValue(generator = USER_ID_SEQUENCE)
	@Id
	private Long id;

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<Role> roles = new HashSet<>();

	public UserDao(String username, String email, String password, Set<Role> roles) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

}
