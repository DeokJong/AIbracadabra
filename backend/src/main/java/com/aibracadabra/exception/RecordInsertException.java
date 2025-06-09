package com.aibracadabra.exception;

@SuppressWarnings("serial")
public class RecordInsertException extends RuntimeException {
	public RecordInsertException() {
		this("데이터 삽입 오류");
	}

	public RecordInsertException(String message) {
		super(message);
	}
}
