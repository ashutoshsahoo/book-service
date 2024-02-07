package com.ashu.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 4802305410875463159L;

	private String message;

	private List<String> details;
}
