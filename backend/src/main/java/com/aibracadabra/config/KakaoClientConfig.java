package com.aibracadabra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class KakaoClientConfig {
    @Value("${spring.web-client.kakao-base-url}")
    private String kakaoBaseUrl;

    @Value("${spring.web-client.kakao-api-key}")
    private String kakaoApiKey;

    @Bean
    WebClient kakaoApiWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(kakaoBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
                .build();
    }

}
