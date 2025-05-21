package com.ssafy.model.service.impl;

import com.ssafy.model.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMemory redisChatMemory;
    private final ChatClient chatClient;

    @Override
    public void clearChatMemory(String identificationKey) {
        redisChatMemory.clear(identificationKey);
    }

    @Override
    public Object chatRecommendSchedule(String identificationKey, String userInput) {
        return chatClient.prompt(userInput).advisors(advisorSpec -> advisorSpec.param("chat_memory_conversation_id", identificationKey)).call().entity(new ParameterizedTypeReference<>() {
        });
    }

    @Override
    public List<Message> chatHistory(String identificationKey) {
        return redisChatMemory.get(identificationKey, Integer.MAX_VALUE);
    }
}
