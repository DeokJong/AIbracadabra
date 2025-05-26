package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.HotPlace;
import com.ssafy.model.service.HotPlaceService;
import com.ssafy.security.dto.CustomUserDetails;

import groovy.transform.builder.Builder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/hotPlace")
@RequiredArgsConstructor
public class RestHotPlaceControllerImpl implements ResponseEntityHelper, RestHotPlaceController {

	private final HotPlaceService hotPlaceService;

	@Override
	public ResponseEntity<?> registHotPlace(
			HotPlace hotPlace,
			MultipartFile image,
			CustomUserDetails user) {
		hotPlace.setMno(user.getMember().getMno());
		hotPlaceService.registHotPlace(hotPlace, image);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "CREATED"));
	}

	@Override
	public ResponseEntity<?> searchByLocation(
			String mapX,
			String mapY,
			String meter) {
		return handleResponse(
				hotPlaceService.findByLocation(mapX, mapY, meter),
				"OK",
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> searchByHno(Integer hno) {
		return handleResponse(hotPlaceService.findByHno(hno), "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteByHno(Integer hno, CustomUserDetails user) {
		hotPlaceService.deleteByHno(hno);
		return handleResponse("NO_CONTENT", HttpStatus.NO_CONTENT);
	}
}
