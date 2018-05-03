package com.henu.exception;

public class UserNotExistException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserNotExistException() {
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
