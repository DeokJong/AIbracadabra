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
public class Coord2AddressResponse {
    private Meta meta;
    private List<Coord2AddressDocument> documents;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Meta {
        private int total_count;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Coord2AddressDocument {
        private AddressSearchResponse.AddressInfo address;          // 위에서 정의한 AddressInfo와 유사
        private AddressSearchResponse.RoadAddressInfo road_address; // 위에서 정의한 RoadAddressInfo와 유사
    }
}
