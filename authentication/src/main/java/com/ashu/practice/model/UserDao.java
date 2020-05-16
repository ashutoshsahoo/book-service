package com.ashu.practice.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter 
@NoArgsConstructor
@Table(name = "USERS", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") 
	})
public class UserDao implements Serializable {

	private static final long serialVersionUID = 2648537870798922434L;
	
	private static final String USER_ID_SEQUENCE = "USER_ID_SEQUENCE";
	
	@SequenceGenerator(name = USER_ID_SEQUENCE, sequenceName = USER_ID_SEQUENCE, allocationSize = 1, initialValue = 1000)
	@GeneratedValue(generator = USER_ID_SEQUENCE)
	@Id
	private Long id;

	@NotBlank
	@Size(min=3,max = 20)
	private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@JsonIgnore
	@NotBlank
	@Size(max = 120)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "USER_ROLES", 
				joinColumns = @JoinColumn(name = "USER_ID"), 
				inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<Role> roles = new HashSet<>();

	public UserDao(String username,String email,String password, Set<Role> roles) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	

}
