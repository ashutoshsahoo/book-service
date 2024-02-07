package com.ashu.practice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {

	private static final long serialVersionUID = -7891510884821585699L;

	private static final String BOOK_ID_SEQUENCE = "BOOK_ID_SEQUENCE";

	@Id
	@SequenceGenerator(name = BOOK_ID_SEQUENCE, sequenceName = BOOK_ID_SEQUENCE, allocationSize = 1, initialValue = 1000)
	@GeneratedValue(generator = BOOK_ID_SEQUENCE)
	private Long id;

	@Column(length = 13, nullable = false, unique = true)
	private String isbn;

	@Column(length = 30, nullable = false)
	private String name;

	@Column(length = 30)
	private String author;

}
