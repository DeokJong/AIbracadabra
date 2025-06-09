package com.aibracadabra.model.dto.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "키워드 기반 장소 검색 응답 객체")
public class SearchResponse {

    @Schema(description = "검색 메타 정보")
    private Meta meta;

    @Schema(description = "검색 결과 문서 리스트")
    private List<KeywordSearchDocument> documents;

    @Getter
    @Setter
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "검색 메타 정보")
    public static class Meta {

        @JsonProperty("total_count")
        @Schema(description = "전체 검색 결과 수", example = "3")
        private int total_count;

        @JsonProperty("pageable_count")
        @Schema(description = "페이지당 노출 수", example = "3")
        private int pageable_count;

        @JsonProperty("is_end")
        @Schema(description = "마지막 페이지 여부", example = "true")
        private boolean is_end;

        @JsonProperty("same_name")
        @Schema(description = "동일 키워드 정보")
        private SameNameInfo same_name;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "동일 키워드 관련 추가 정보")
    public static class SameNameInfo {

        @Schema(description = "지역 이름 리스트", example = "[\"서울특별시\",\"경기도\"]")
        private List<String> region;

        @Schema(description = "검색 키워드", example = "강남역")
        private String keyword;

        @JsonProperty("selected_region")
        @Schema(description = "선택된 지역", example = "서울특별시")
        private String selected_region;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "키워드 검색 결과 문서")
    public static class KeywordSearchDocument {

        @Schema(description = "문서 ID", example = "1234567890")
        private String id;

        @JsonProperty("place_name")
        @Schema(description = "장소 이름", example = "스타벅스 강남점")
        private String place_name;

        @JsonProperty("category_name")
        @Schema(description = "카테고리 전체 이름", example = "음식점 > 카페")
        private String category_name;

        @JsonProperty("category_group_code")
        @Schema(description = "카테고리 그룹 코드", example = "FD6")
        private String category_group_code;

        @JsonProperty("category_group_name")
        @Schema(description = "카테고리 그룹 이름", example = "음식점")
        private String category_group_name;

        @Schema(description = "전화번호", example = "02-1234-5678")
        private String phone;

        @JsonProperty("address_name")
        @Schema(description = "지번 주소", example = "서울특별시 강남구 역삼동 123-45")
        private String address_name;


        @JsonProperty("road_address_name")
        @Schema(description = "도로명 주소", example = "서울특별시 강남구 테헤란로 123")
        private String road_address_name;

        @JsonProperty("x")
        @Schema(description = "X 좌표 (경도)", example = "127.0276368")
        private String x;

        @JsonProperty("y")
        @Schema(description = "Y 좌표 (위도)", example = "37.4979507")
        private String y;

        @JsonProperty("place_url")
        @Schema(description = "장소 상세 URL", example = "http://place.map.kakao.com/1234567890")
        private String place_url;

        @Schema(description = "검색 중심으로부터의 거리(미터 단위)", example = "150")
        private String distance;
    }
}
