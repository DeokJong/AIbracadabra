package com.ssafy.restcontroller;

import com.ssafy.model.service.ChatService;
import com.ssafy.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class RestChatControllerImpl implements ResponseEntityHelper, RestChatController {

	private final ChatService chatService;

	@Override
	public ResponseEntity<?> recommend(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user,

		@Parameter(
			name = "userInput",
			description = "추천을 요청할 사용자의 입력 텍스트",
			example = "주말에 서울에서 갈 만한 전시 추천해줘"
		)
		@RequestParam String userInput
	) {
		String identificationKey = user.getUsername();
		return handleResponse(
			chatService.chatRecommendSchedule(identificationKey, userInput),
			"OK",
			HttpStatus.OK
		);
	}

	@Override
	public ResponseEntity<?> chatHistory(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user
	) {
		String identificationKey = user.getUsername();
		List<Message> messages = chatService.chatHistory(identificationKey);
		return handleResponse(messages, "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> cleanChatMemory(
		@Parameter(hidden = true)
		@AuthenticationPrincipal CustomUserDetails user
	) {
		String identificationKey = user.getUsername();
		chatService.clearChatMemory(identificationKey);
		return handleResponse("NO_CONTENT", "OK", HttpStatus.NO_CONTENT);
	}

}
