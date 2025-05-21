package com.ssafy.config;

import com.ssafy.util.RuntimeTypeAdapterFactory;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DefaultRedisConfig {
	@Value("${spring.data.redis.host}")
	String host;
	@Value("${spring.data.redis.port}")
	Integer port;

	@Bean
	RedisConnectionFactory defaultRedisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	RuntimeTypeAdapterFactory<Message> messageTypeAdapterFactory() {
		return RuntimeTypeAdapterFactory.of(Message.class, "messageType",true)
				.registerSubtype(UserMessage.class, MessageType.USER.name())
				.registerSubtype(AssistantMessage.class, MessageType.ASSISTANT.name())
				.registerSubtype(SystemMessage.class, MessageType.SYSTEM.name())
				.registerSubtype(ToolResponseMessage.class, MessageType.TOOL.name());
	}
}
