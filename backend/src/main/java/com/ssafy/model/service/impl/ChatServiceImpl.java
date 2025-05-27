package com.ssafy.model.service.impl;

import com.ssafy.model.dto.domain.RecommendChat;
import com.ssafy.model.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ChatServiceImpl implements ChatService {
  private final ChatMemory redisChatMemory;
  private final ChatClient recommendChatClient;
  private final ChatClient personalityChatClient;

  public ChatServiceImpl(ChatMemory redisChatMemory,
                         @Qualifier("recommendChatClient") ChatClient recommendChatClient,
                         @Qualifier("personalityChatClient") ChatClient personalityChatClient) {
    this.redisChatMemory = redisChatMemory;
    this.recommendChatClient = recommendChatClient;
    this.personalityChatClient = personalityChatClient;
  }

  @Override
  public void clearChatMemory(String identificationKey) {
    redisChatMemory.clear(identificationKey);
  }

  @Override
  public Object chatRecommendSchedule(String identificationKey, String userInput) {
    return recommendChatClient.prompt(userInput).advisors(advisorSpec -> advisorSpec.param("chat_memory_conversation_id", identificationKey)).call().entity(new ParameterizedTypeReference<RecommendChat>() {
    });
  }

  @Override
  public List<Message> chatHistory(String identificationKey) {
    return redisChatMemory.get(identificationKey, Integer.MAX_VALUE);
  }
}
