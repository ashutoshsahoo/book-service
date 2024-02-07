package com.ashu.practice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

	private static final long serialVersionUID = -3802416542766728846L;

	private static final String ROLE_ID_SEQUENCE = "ROLE_ID_SEQUENCE";

	@SequenceGenerator(name = ROLE_ID_SEQUENCE, sequenceName = ROLE_ID_SEQUENCE, allocationSize = 1, initialValue = 1000)
	@GeneratedValue(generator = ROLE_ID_SEQUENCE)
	@Id
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private RoleType name;

	public Role(RoleType name) {
		super();
		this.name = name;
	}

}
