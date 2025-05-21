package com.ssafy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.restcontroller.ResponseEntityHelper;

@RestControllerAdvice
public class _RestGlobalExceptionHandler implements ResponseEntityHelper {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
		return handleFail(ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MismatchException.class)
	public ResponseEntity<?> handleBadRequest(MismatchException ex) {
		return handleFail(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> handleForbidden(ForbiddenException ex) {
		return handleFail(ex, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> handleNotFound(RecordNotFoundException ex) {
		return handleFail(ex, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicatedException.class)
	public ResponseEntity<?> handleConflict(DuplicatedException ex) {
		return handleFail(ex, HttpStatus.CONFLICT);
	}

}