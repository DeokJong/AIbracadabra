package com.ssafy.model.dto.client;

import lombok.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 응답 메시지와 여행 일정 플랜을 담는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GptResponse {

    /** 사용자에게 보여줄 메시지 */
    @NonNull
    private String message;

    /** 세부 일정 리스트 */
    private List<Schedule> schedule;

    /**
     * 세분화된 일정 정보
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Schedule {
        /** 일정의 순서(몇 번째 날) */
        private int day;

        /** 일정 시작 시각 */
        private LocalDateTime startDateTime;

        /** 일정 종료 시각 */
        private LocalDateTime endDateTime;

        /** 소요 시간 */
        private Duration duration;

        /** 활동 제목 */
        private String activityName;

        /** 활동 상세 설명 */
        private String activityDescription;

        /** 활동 장소 이름 */
        private String locationName;

        /** 활동 장소 주소 */
        private String address;

        /** 위도 */
        private Double latitude;

        /** 경도 */
        private Double longitude;
    }
}
