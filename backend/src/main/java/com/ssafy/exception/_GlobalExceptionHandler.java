package com.ssafy.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ssafy.restcontroller.ResponseEntityHelper;

// @ControllerAdvice
public class _GlobalExceptionHandler implements ResponseEntityHelper {

	@ExceptionHandler(value = UnauthorizedException.class)
	public String handleUnauthorized() {
		return "redirect:/auth/login";
	}

	@ExceptionHandler(MismatchException.class)
	public String handleBadRequest() {
		return "error/400";
	}

	@ExceptionHandler(ForbiddenException.class)
	public String handleForbidden() {
		return "error/403";
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public String handleNotFound() {
		return "error/404";
	}

	@ExceptionHandler(DuplicatedException.class)
	public String handleDuplicated() {
		return "error/409";
	}
}
