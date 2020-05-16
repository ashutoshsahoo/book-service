package com.ashu.practice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = 4802305410875463159L;

	private String message;

	private List<String> details;
}
