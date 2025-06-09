package com.aibracadabra.model.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AddressSearchResponse {
    private Meta meta;
    private List<AddressSearchDocument> documents;

    // + getters/setters
    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class AddressSearchDocument {
        private String address_name;
        private String address_type;
        private String x;
        private String y;
        private AddressInfo address;
        private RoadAddressInfo road_address;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class AddressInfo {
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String region_3depth_h_name;
        private String h_code;
        private String b_code;
        private String mountain_yn;
        private String main_address_no;
        private String sub_address_no;
        private String x;
        private String y;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class RoadAddressInfo {
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String road_name;
        private String underground_yn;
        private String main_building_no;
        private String sub_building_no;
        private String building_name;
        private String zone_no;
        private String y;
        private String x;
    }
}
