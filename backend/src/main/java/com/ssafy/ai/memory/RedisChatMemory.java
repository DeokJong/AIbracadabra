package com.ssafy.ai.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ssafy.util.RuntimeTypeAdapterFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RedisChatMemory implements ChatMemory {
    private static final String PREFIX = "chat:mem:";
    private final RedisTemplate<String, String> conversationHistory;
    private final Gson gson;

    public RedisChatMemory(RedisTemplate<String, String> conversationHistory,
                           RuntimeTypeAdapterFactory<Message> typeAdapterFactory) {
        this.conversationHistory = conversationHistory;
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory)
                .create();
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (conversationId == null) {
            throw new NullPointerException("conversationId is null");
        }

        String key = PREFIX + conversationId;

        List<String> payload = messages.stream()
                .map(gson::toJson)
                .toList();

        conversationHistory.opsForList().rightPushAll(key, payload);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        if(conversationId == null) {
            throw new NullPointerException("conversationId is null");
        }
        if (lastN <= 0) {
            return List.of();
        }
        String key = PREFIX + conversationId;
        ListOperations<String, String> ops = conversationHistory.opsForList();

        return Optional.ofNullable(ops.range(key, -lastN, -1))
                .orElse(Collections.emptyList())
                .stream()
                .map(s -> gson.fromJson(s, Message.class))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        if (conversationId == null) {
            throw new NullPointerException("conversationId is null");
        }
        String key = PREFIX + conversationId;
        conversationHistory.delete(key);
    }

}
