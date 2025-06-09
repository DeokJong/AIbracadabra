package com.aibracadabra.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Albracadabra API",
                version = "1.0.0 M1",
                description = "여행 정보 가이드 및 LLM 기반 추천 서비스, CRUD 기능을 제공하는 API 문서",
                contact = @Contact(
                        name = "SSAFY 13기 광주5반 8팀 진덕종(팀장), 김태민(팀원)",
                        email = "novelss5573@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "개발용 서버"),
        }
)
public class OpenApiConfig {
}
