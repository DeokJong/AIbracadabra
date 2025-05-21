package com.ssafy.exception;

/**
 * 400 BAD REQUEST 내릴 것
 */
@SuppressWarnings("serial")
public class MismatchException extends RuntimeException {
	public MismatchException(String message) {
		super(message);
	}
	
	public MismatchException() {
		this("두 값이 일치하지 않습니다.");
	}
}
