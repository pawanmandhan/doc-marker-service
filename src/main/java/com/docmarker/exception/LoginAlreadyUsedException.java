package com.docmarker.exception;

public class LoginAlreadyUsedException extends RuntimeException {

	public LoginAlreadyUsedException() {
	}

	public LoginAlreadyUsedException(String msg) {
		super(msg);
	}
}