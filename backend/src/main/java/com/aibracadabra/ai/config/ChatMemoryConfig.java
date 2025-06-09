package com.aibracadabra.ai.config;

import com.aibracadabra.ai.memory.RedisChatMemory;
import com.aibracadabra.util.RuntimeTypeAdapterFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ChatMemoryConfig {
    @Bean
    ChatMemory inMemoryChatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    @Primary
    ChatMemory redisChatMemory(RedisTemplate<String, String> conversationHistory,
                               @Qualifier("messageTypeAdapterFactory") RuntimeTypeAdapterFactory<Message> typeAdapterFactory) {
        return new RedisChatMemory(conversationHistory, typeAdapterFactory);
    }
}
