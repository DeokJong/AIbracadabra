package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.Plan;
import com.ssafy.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Plan", description = "여행 계획 API")
@RequestMapping("/api/v1/plans")
public interface RestPlanController {

	/**
	 * 로그인한 멤버 기준 plan 생성
	 * @param userDetails 로그인한 멤버 정보
	 * @param plan 생성할 plan 정보
	 * @return 응답 객체
	 */
	@Operation(summary = "여행 계획 생성", description = "로그인한 사용자의 mno를 기준으로 새로운 여행 계획을 DB에 저장합니다.")
	@PostMapping
	ResponseEntity<?> createPlan(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody Plan plan
	);

	/**
	 * pno 기준 해당 plan 업데이트
	 * @param pno 업데이트할 pno
	 * @param plan 업데이트할 plan 정보
	 * @return 응답 객체
	 */
	@Operation(summary = "여행 계획 수정", description = "지정된 PNO의 여행 계획 정보를 업데이트합니다.")
	@PutMapping("/{pno}")
	ResponseEntity<?> updatePlan(
		@PathVariable("pno") Integer pno,
		@RequestBody Plan plan
	);

	/**
	 * pno 기준 해당 plan 제거
	 * @param pno 제거할 pno
	 * @return 응답 객체
	 */
	@Operation(summary = "여행 계획 삭제", description = "지정된 PNO의 여행 계획을 삭제합니다.")
	@DeleteMapping("/{pno}")
	ResponseEntity<?> deletePlan(@PathVariable("pno") Integer pno);

	/**
	 * 로그인한 멤버 기준으로 해당 멤버의 모든 Plan 조회
	 * @param userDetails 로그인한 멤버 정보
	 * @return 응답 객체
	 */
	@Operation(summary = "내 여행 계획 목록 조회", description = "로그인한 사용자의 모든 여행 계획을 조회합니다.")
	@GetMapping("/myplan")
	ResponseEntity<?> getPlansByMember(@AuthenticationPrincipal CustomUserDetails userDetails);

	/**
	 * pno 기준 Plan 조회
	 * @param pno 조회할 pno
	 * @return 응답 객체
	 */
	@Operation(summary = "여행 계획 단건 조회", description = "지정된 PNO의 여행 계획 상세 정보를 조회합니다.")
	@GetMapping("/{pno}")
	ResponseEntity<?> getPlanByPno(@PathVariable("pno") Integer pno);

}
