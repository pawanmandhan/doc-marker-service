package com.docmarker.web.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenericResponse {

	private Object message;
	private String error;

	public GenericResponse() {
	}

	public GenericResponse(final Object message) {
		super();
		this.message = message;
	}

	public GenericResponse(final Object message, final String error) {
		super();
		this.message = message;
		this.error = error;
	}

	public GenericResponse(List<ObjectError> allErrors, String error) {
		this.error = error;
		String temp = allErrors.stream().map(e -> {
			if (e instanceof FieldError) {
				return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\""
						+ e.getDefaultMessage() + "\"}";
			} else {
				return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage()
						+ "\"}";
			}
		}).collect(Collectors.joining(","));
		this.message = "[" + temp + "]";
	}

}
