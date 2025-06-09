package com.aibracadabra.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException() {
		this("로그인 되지 않은 사용자");
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
