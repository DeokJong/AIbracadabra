package com.aibracadabra.restcontroller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Image", description = "ino 기준 이미지만 조회하도록 하는 API")
public interface RestImageController {

	@Operation(summary = "이미지 조회", description = "권한 검사 후 이미지 반환")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/{ino}")
	ResponseEntity<?> serveImage(@PathVariable Integer ino);

}
