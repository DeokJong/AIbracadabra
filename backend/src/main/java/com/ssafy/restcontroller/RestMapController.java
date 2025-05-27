package com.ssafy.restcontroller;

import com.ssafy.constant.ContentTypeId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Tag(name = "Map", description = "지도 API")
public interface RestMapController {

	@Operation(summary = "주소 검색", description = "주소를 검색합니다.")
	@GetMapping("location")
	Mono<ResponseEntity<?>> searchLocation(@RequestParam String query,
																				 @RequestParam(defaultValue = "1") Integer pageNo);

	@Operation(summary = "주변 컨텐츠 조회", description = "위도, 경도 값과 컨텐츠 값을 이용해서 100개의 주변 상권 정보를 갖고 옵니다.")
	@GetMapping("/contents")
	Mono<ResponseEntity<?>> searchContents(@RequestParam(defaultValue = "126.978652258309") String mapX,
																				 @RequestParam(defaultValue = "37.566826004661") String mapY,
																				 @RequestParam(defaultValue = "20000") String radius,
																				 @RequestParam ContentTypeId contentTypeId,
																				 @RequestParam(defaultValue = "1") Integer pageNo);


	@Operation(summary = "컨텐츠 상세 조회", description = "contentId를 이용해서 컨텐츠의 상세 정보를 조회 합니다.")
	@GetMapping("/contents/{contentId}")
	Mono<ResponseEntity<?>> searchContentDetail(@PathVariable String contentId);
}
