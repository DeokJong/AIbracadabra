package com.aibracadabra.restcontroller;

import com.aibracadabra.model.service.ChatService;
import com.aibracadabra.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class RestChatControllerImpl implements ResponseEntityHelper, RestChatController {

	private final ChatService chatService;

	@Override
	public ResponseEntity<?> recommend(CustomUserDetails user, String userInput) {
		String identificationKey = user.getUsername();
		return handleResponse(chatService.chatRecommendSchedule(identificationKey, userInput), "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> chatHistory(CustomUserDetails user) {
		String identificationKey = user.getUsername();
		List<Message> messages = chatService.chatHistory(identificationKey);
		return handleResponse(messages, "OK", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> cleanChatMemory(CustomUserDetails user) {
		String identificationKey = user.getUsername();
		chatService.clearChatMemory(identificationKey);
		return handleResponse("NO_CONTENT", "OK", HttpStatus.NO_CONTENT);
	}

}
