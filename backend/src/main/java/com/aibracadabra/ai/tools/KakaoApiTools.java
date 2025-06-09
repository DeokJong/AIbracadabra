package com.aibracadabra.ai.tools;

import com.aibracadabra.model.dto.client.SearchResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;


public interface KakaoApiTools {
  @Tool(name = "searchByLocationAndCategoryAndMeter", description = "입력된 Location과 PlaceCategory 코드를 기반으로 해당 Location 주변 meter 이내의 장소를 검색합니다.")
  List<SearchResponse.KeywordSearchDocument> searchByLocationAndCategoryAndMeter(
          @ToolParam(description = "검색어, 예: \"서울 남산 타워\"") String location,
          @ToolParam(description = """
                  장소 카테고리
                  [
                    { "code": "MT1", "description": "대형마트" },
                    { "code": "CS2", "description": "편의점" },
                    { "code": "PK6", "description": "주차장" },
                    { "code": "OL7", "description": "주유소, 충전소" },
                    { "code": "SW8", "description": "지하철역" },
                    { "code": "BK9", "description": "은행" },
                    { "code": "CT1", "description": "문화시설" },
                    { "code": "AG2", "description": "중개업소" },
                    { "code": "PO3", "description": "공공기관" },
                    { "code": "AT4", "description": "관광명소" },
                    { "code": "AD5", "description": "숙박" },
                    { "code": "FD6", "description": "음식점" },
                    { "code": "CE7", "description": "카페" },
                    { "code": "HP8", "description": "병원" },
                    { "code": "PM9", "description": "약국" }
                  ]
                  """)
          String placeCategory,
          @ToolParam(description = "검색 반경(미터 단위), 예: 20000") Integer meter);
}
