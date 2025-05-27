package com.ssafy.model.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TourInfoResponse {

    @JsonProperty("response")
    private Response response;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Body {

        // 커스텀 deserializer 적용
        @JsonDeserialize(using = ItemsDeserializer.class)
        private Items items;

        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Items {
        private List<Item> item;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        private String addr1;
        private String addr2;
        private Integer areacode;
        private String booktour;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentid;
        private String contenttypeid;
        private Long createdtime;
        private String firstimage;
        private String firstimage2;
        private String mapx;
        private String mapy;
        private Integer mlevel;
        private Long modifiedtime;
        private Integer readcount;
        private Integer sigungucode;
        private String tel;
        private String title;
        private String zipcode;
        private String homepage;
        private String overview;
        private Integer rnum;
        private String code;
        private String name;
    }
}
