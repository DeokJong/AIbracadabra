package com.ssafy.exception;

@SuppressWarnings("serial")
public class RecordNotFoundException extends RuntimeException {
	public RecordNotFoundException() {
		this("해당 데이터를 찾지 못함");
	}

	public RecordNotFoundException(String message) {
		super(message);
	}
}
