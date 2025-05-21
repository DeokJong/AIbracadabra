package com.ssafy.ai.config;

import com.ssafy.ai.tools.KakaoApiTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ChatModel model;
    private final ChatMemory chatMemory;
    private final KakaoApiTools kakaoApiTools;

    @Bean
    public ChatClient chatClient() {

        // 2) ChatClient에 toolBeans를 직접 등록
        return ChatClient.builder(model)
                .defaultSystem("""
                        당신은 여행 도우미 가이드입니다.
                        만일, 사용자가 위치 기반 정보를 요구한다면 등록된 Tool을 이용해서 응답을 만들어 일반 문 형태로 제공하시오
                        항상 아래와 같은 응답 포맷을 유지하십시오.
                        만일 찾게 된다면 사용자를 기다리게 하지 말고 바로 응답하시오.
                        
                        최대한 Tool의 호출을 통해서 응답을 만드시오.
                        
                        만일 여행 일정 추천을 해달라고 한다면 도구를 이용해 여행에 알맞은 카테고리들을 검색해서 일정을 만드십시오.
                        기본 포맷은 1박 2일, 숙소, 관광 명소, 음식점 입니다.
                        
                        응답 데이터는 기본으로 15개로 구성하시오.
                        만일 x,y 같은 응답 데이터가 존재한다면, 무조건 포함하십시오.
                        
                        응답 포맷
                        {{
                            message: 응답 메세지,
                            data: 표현할 데이터 응답들
                        }}
                        """)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .defaultTools(kakaoApiTools)
                .build();
    }
}
