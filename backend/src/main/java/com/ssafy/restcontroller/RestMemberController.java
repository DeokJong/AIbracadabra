package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.dto.request.SignupRequest;
import com.ssafy.model.dto.request.UpdateMemberRequest;
import com.ssafy.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 정보 관리 API")
public interface RestMemberController {

	@Operation(summary = "현재 로그인 사용자 조회", description = "인증된 사용자의 정보를 반환합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "조회 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Member.class)
			)
		),
		@ApiResponse(responseCode = "401", description = "인증되지 않음")
	})
	@GetMapping("/me")
	ResponseEntity<?> loginUserInfo(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails principal
	);

	@Operation(summary = "회원 정보 수정", description = "로그인한 회원의 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
	})
	@PutMapping
	ResponseEntity<?> update(
		@AuthenticationPrincipal CustomUserDetails principal,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "수정할 회원 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UpdateMemberRequest.class)
			)
		) @RequestBody UpdateMemberRequest updateMemberRequest);

	@Operation(summary = "회원 탈퇴", description = "로그인한 회원의 정보를 제거합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "삭제 성공"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
	})
	@DeleteMapping("")
	ResponseEntity<?> delete(
		@AuthenticationPrincipal CustomUserDetails principal
	);

	@Operation(summary = "회원 가입", description = "새 회원을 등록합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "회원 가입 성공",
			content = @Content(
				mediaType = "application/json",
				schemaProperties = {
					@SchemaProperty(
						name = "message",
						schema = @Schema(type = "string", example = "CREATED")
					),
				}
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 바디")
	})
	@PostMapping("/signup")
	ResponseEntity<?> signUp(@RequestBody SignupRequest request);
}
