package com.aibracadabra.model.service;

import org.springframework.ai.chat.messages.Message;

import java.util.List;

public interface ChatService {

    void clearChatMemory(String identificationKey);

    Object chatRecommendSchedule(String identificationKey, String userInput);
    List<Message> chatHistory(String identificationKey);
}
