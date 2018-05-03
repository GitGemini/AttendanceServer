package com.henu.exception;

public class UserChangeExceptiom extends Exception {
	private static final long serialVersionUID = 1L;

	public UserChangeExceptiom() {
		
	}

	public UserChangeExceptiom(String message) {
		super(message);
	}

	public UserChangeExceptiom(Throwable cause) {
		super(cause);
	}

	public UserChangeExceptiom(String message, Throwable cause) {
		super(message, cause);
	}

	public UserChangeExceptiom(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	}

}
