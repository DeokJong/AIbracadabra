package com.aibracadabra.restcontroller;

import com.aibracadabra.model.dto.domain.RecommendChat;
import com.aibracadabra.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "RecommendChat", description = "채팅 기반 추천 및 히스토리 관리 API")
public interface RestChatController {
	@Operation(
		summary = "일정 추천 채팅",
		description = "사용자의 입력에 따라 스케줄 추천 대화를 실행하고 결과 응답 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "추천 대화 성공", content = @Content(
			mediaType = "application/json",
			schema = @Schema(implementation = RecommendChat.class)
		))
	})
	@GetMapping("/recommend")
	ResponseEntity<?> recommend(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user,

		@Parameter(
			name = "userInput",
			description = "추천을 요청할 사용자의 입력 텍스트",
			example = "주말에 서울에서 갈 만한 전시 추천해줘"
		)
		@RequestParam String userInput
	);

	@Operation(
		summary = "채팅 히스토리 조회",
		description = "현재 로그인한 사용자의 채팅 히스토리를 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "히스토리 조회 성공", content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(
				schema = @Schema(implementation = Message.class)
			)
		)),
		@ApiResponse(responseCode = "401", description = "인증되지 않음")
	})
	@GetMapping("/chatHistory")
	ResponseEntity<?> chatHistory(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user
	);

	@ApiResponses({
		@ApiResponse(
			responseCode = "204",
			description = "메모리 초기화 완료",
			content = @Content(
				schema = @Schema(implementation = Void.class)
			)
		),
		@ApiResponse(responseCode = "401", description = "인증되지 않음")
	})
	@PostMapping("/cleanChatMemory")
	ResponseEntity<?> cleanChatMemory(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user
	);
}
