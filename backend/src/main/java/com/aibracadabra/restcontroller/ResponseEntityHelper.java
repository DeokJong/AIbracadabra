package com.aibracadabra.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ResponseEntityHelper {
	/**
	 * 결과값을 받아 기본 설정값으로 상태 코드 200으로 내림
	 *
	 * @param data
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleSuccess(Object data) {
		return handleSuccess(data, HttpStatus.OK);
	}

	/**
	 * 에러값을 받아 기본 설정값으로 상태 코드 500으로 내림
	 *
	 * @param e
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleFail(Exception e) {
		return handleFail(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 결과 값과 상태 코드를 받아서 리턴함.
	 *
	 * @param data
	 * @param status
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleSuccess(Object data, HttpStatus status) {
		Map<String, Object> map = Map.of("status", "SUCCESS", "data", data);
		return ResponseEntity.status(status).body(map);
	}

	/**
	 * 에러 값과 상태 코드를 받아서 리턴함.
	 *
	 * @param e
	 * @param status
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleFail(Exception e, HttpStatus status) {
		Map<String, Object> map = Map.of("status", "FAIL", "message", e.getMessage());
		return ResponseEntity.status(status).body(map);
	}

	/**
	 * 에러 값과 상태 코드를 받아서 리턴함.
	 *
	 * @param e
	 * @param status
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleSimpleMessageFail(Exception e, HttpStatus status) {
		Map<String, Object> map = Map.of("status", "FAIL", "message", e.getMessage());
		return ResponseEntity.status(status).body(map);
	}


	/**
	 * 결과 값과 상태 코드, 상태 메세지를 받아서 리턴함.
	 *
	 * @param statusMessage
	 * @param status
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleResponse(Object data, String statusMessage, HttpStatus status) {
		Map<String, Object> map = Map.of(status.isError() ? "message" : "status", statusMessage, "data", data);
		return ResponseEntity.status(status).body(map);
	}

	/**
	 * 상태 코드, 상태 메세지를 받아서 리턴함.
	 *
	 * @param statusMessage
	 * @param status
	 * @return ResponseEntity
	 */
	default ResponseEntity<?> handleResponse(String statusMessage, HttpStatus status) {
		Map<String, Object> map = Map.of(status.isError() ? "message" : "status", statusMessage);
		return ResponseEntity.status(status).body(map);
	}

}
