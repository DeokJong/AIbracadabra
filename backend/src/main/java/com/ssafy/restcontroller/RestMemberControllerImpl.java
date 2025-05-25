package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.Member;
import com.ssafy.model.dto.request.SignupRequest;
import com.ssafy.model.dto.request.UpdateMemberRequest;
import com.ssafy.model.service.MemberService;
import com.ssafy.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class RestMemberControllerImpl implements ResponseEntityHelper, RestMemberController {

	private final MemberService memberService;

	@Override
	public ResponseEntity<?> loginUserInfo(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails principal
	) {
		Member m = principal.getMember();
		return handleResponse(m, "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> update(
		@AuthenticationPrincipal CustomUserDetails principal,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "수정할 회원 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = UpdateMemberRequest.class)
			)
		) @RequestBody UpdateMemberRequest updateMemberRequest) {

		updateMemberRequest.setMno(principal.getMember().getMno());
		int result = memberService.set(updateMemberRequest);
		if (result == 0) {
			throw new RuntimeException("예상치 못한 흐름: 수정 실패");
		}
		return handleResponse("NO_CONTENT", HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> delete(
		@AuthenticationPrincipal CustomUserDetails principal
	) {
		int result = memberService.remove(principal.getMember().getMno());
		if (result == 0) {
			throw new RuntimeException("예상치 못한 흐름: 삭제 실패");
		}
		return handleResponse("NO_CONTENT", HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
		Member member = Member.builder()
			.email(request.getEmail())
			.name(request.getName())
			.password(request.getPassword())
			.role(request.getRole())
			.build();

		int result = memberService.signUp(member, request.getCheckPassword());
		if (result == 0) {
			throw new RuntimeException("예상치 못한 흐름: 가입 실패");
		}
		return handleResponse("CREATED", HttpStatus.CREATED);
	}
}
