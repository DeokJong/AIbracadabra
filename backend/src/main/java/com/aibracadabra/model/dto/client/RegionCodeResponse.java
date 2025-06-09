package com.aibracadabra.model.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegionCodeResponse {
    private Meta meta;
    private List<RegionCodeDocument> documents;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Meta {
        private int total_count;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class RegionCodeDocument {
        private String region_type;       // H or B
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String region_4depth_name; // 선택적
        private String code;
        private double x;
        private double y;
    }
}

