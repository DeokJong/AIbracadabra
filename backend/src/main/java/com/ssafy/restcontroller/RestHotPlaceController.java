package com.ssafy.restcontroller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.model.dto.domain.HotPlace;
import com.ssafy.security.dto.CustomUserDetails;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface RestHotPlaceController {

	@RequestBody(content = @Content(encoding = {
			@Encoding(name = "hotPlace", contentType = MediaType.APPLICATION_JSON_VALUE),
			@Encoding(name = "image", contentType = MediaType.MULTIPART_FORM_DATA_VALUE) }))
	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> registHotPlace(@RequestPart HotPlace hotPlace,
			@RequestPart(required = false) MultipartFile image,
			@AuthenticationPrincipal CustomUserDetails user);

	@GetMapping
	ResponseEntity<?> searchByLocation(@RequestParam String mapX,
			@RequestParam String mapY,
			@RequestParam(defaultValue = "20000") String meter);

	@GetMapping("/{hno}")
	ResponseEntity<?> searchByHno(@PathVariable Integer hno);

	@DeleteMapping("/{hno}")
	ResponseEntity<?> deleteByHno(@PathVariable Integer hno, @AuthenticationPrincipal CustomUserDetails user);
}
