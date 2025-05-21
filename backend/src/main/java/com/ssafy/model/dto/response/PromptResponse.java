package com.ssafy.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "GPT 기반 일정 추천 응답 DTO")
@Data
public class PromptResponse {
    @Schema(
            description = "추천 메시지",
            example = "서울에서 주말에 갈 만한 전시 추천입니다."
    )
    private String message;

    @Schema(
            description = "추천 장소 리스트",
            implementation = Object.class
    )
    private Object data;
}
