package com.ssafy.exception;

@SuppressWarnings("serial")
public class ForbiddenException extends RuntimeException {

	public ForbiddenException() {
		this("허락되지 않은 접근");
	}

	public ForbiddenException(String message) {
		super(message);
	}

}
