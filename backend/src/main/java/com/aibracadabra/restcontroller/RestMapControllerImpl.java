package com.aibracadabra.restcontroller;

import com.aibracadabra.constant.ContentTypeId;
import com.aibracadabra.model.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/map")
@RequiredArgsConstructor
@Validated
public class RestMapControllerImpl implements ResponseEntityHelper, RestMapController {

	private final MapService mapService;

	@Override
	public Mono<ResponseEntity<?>> searchLocation(String query, Integer pageNo) {
		return mapService.searchAddress(query, pageNo).map(raw -> handleResponse(raw, "OK", HttpStatus.OK));
	}


	@Override
	public Mono<ResponseEntity<?>> searchContents(String mapX, String mapY, String radius, ContentTypeId contentTypeId, Integer pageNo) {
		return mapService.searchContent(mapX, mapY, contentTypeId.getCode() , pageNo, radius).map(raw -> handleResponse(raw, "OK", HttpStatus.OK));
	}

	@Override
	public Mono<ResponseEntity<?>> searchContentDetail(String contentId) {
		return mapService.searchContentDetail(contentId).map(raw -> handleResponse(raw, "OK", HttpStatus.OK));
	}
};