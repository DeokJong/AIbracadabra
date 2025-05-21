package com.ssafy.ai.config;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.ai.tools.ContentTypeTools;
import com.ssafy.model.service.UtilService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TestChatClientConfig {

	@Autowired
	@Qualifier("simpleFunctionCallingClient")
	ChatClient simpleFunctionCallingClient;
	
	@Autowired
	ContentTypeTools contentTypeTools;
	
	@Autowired
	UtilService utilService;

	@Test
	void TestFunctionCall() {
		String reply = simpleFunctionCallingClient.prompt()
				.user("여행 카테고리 알려줘")
				.tools("getContentTypes")
				.call()
				.content();
		
		log.info(reply);
	}
}
