package com.ssafy.exception;

@SuppressWarnings("serial")
public class DuplicatedException extends RuntimeException {

	public DuplicatedException() {
		this("데이터 중복");
	}

	public DuplicatedException(String message) {
		super(message);
	}

}
