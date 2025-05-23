package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Document {
    private Meta meta;
    private List<Item> documents;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
        private boolean isEnd;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Jacksonized
    public static class Item {
        private String mapX;
        private String mapY;
        private String title;
        private String tel;
        private String firstImage;
        private String homepage;
        private String address;
        private String overview;
        private String contentsTypeId;
        private String contentId;
    }
}
