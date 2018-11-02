package com.docmarker.exception;

public class EmailAlreadyUsedException extends RuntimeException {

	public EmailAlreadyUsedException() {
	}

	public EmailAlreadyUsedException(String msg) {
		super(msg);
	}
}
