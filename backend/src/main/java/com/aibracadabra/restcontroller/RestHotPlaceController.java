package com.aibracadabra.restcontroller;

import com.aibracadabra.model.dto.domain.HotPlace;
import com.aibracadabra.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "HotPlace", description = "핫플레이스 관련 API")
public interface RestHotPlaceController {

	@RequestBody(content = @Content(encoding = {
		@Encoding(name = "hotPlace", contentType = MediaType.APPLICATION_JSON_VALUE),
		@Encoding(name = "image", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)}))
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
		MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> registerHotPlace(@RequestPart HotPlace hotPlace,
																		 @RequestPart(required = false) MultipartFile image,
																		 @AuthenticationPrincipal CustomUserDetails user);

	@GetMapping
	ResponseEntity<?> searchByLocation(@RequestParam String mapX,
																		 @RequestParam String mapY,
																		 @RequestParam(defaultValue = "20000") String meter);

	@GetMapping("/{hno}")
	ResponseEntity<?> searchByHno(@PathVariable Integer hno);

	@RequestBody(content = @Content(encoding = {
		@Encoding(name = "hotPlace", contentType = MediaType.APPLICATION_JSON_VALUE),
		@Encoding(name = "image", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)}))
	@PutMapping(consumes = {
		MediaType.MULTIPART_FORM_DATA_VALUE,
		MediaType.APPLICATION_JSON_VALUE}
		, produces = MediaType.APPLICATION_JSON_VALUE
		,
		value = "/{hno}")
	ResponseEntity<?> updateHotPlace(@RequestPart HotPlace hotPlace,
																	 @RequestPart(required = false) MultipartFile image,
																	 @PathVariable Integer hno,
																	 @AuthenticationPrincipal CustomUserDetails user);

	@DeleteMapping("/{hno}")
	ResponseEntity<?> deleteByHno(@PathVariable Integer hno, @AuthenticationPrincipal CustomUserDetails user);

	@PostMapping("/like/{hno}")
	ResponseEntity<?> likeHotPlace(@PathVariable Integer hno, @AuthenticationPrincipal CustomUserDetails user);

	@DeleteMapping("/like{hno}")
	ResponseEntity<?> unlikeHotPlace(@PathVariable Integer hno,@AuthenticationPrincipal CustomUserDetails user);

	@GetMapping("/likes/me")
	ResponseEntity<?> findMyLikedHotPlaces(@AuthenticationPrincipal CustomUserDetails user);
}
