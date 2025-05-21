package com.ssafy.ai.tools;

import com.ssafy.model.client.KakaoClient;
import com.ssafy.model.dto.client.SearchResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 도구: 지정된 위치와 카테고리, 반경(미터) 내의 장소를 검색합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiTools {
    private final KakaoClient kakaoClient;

    @Tool(
            name = "searchByLocationAndCategoryAndMeter",
            description = "입력된 Location과 PlaceCategory 코드를 기반으로 해당 Location 주변 meter 이내의 장소를 검색합니다."
    )
    public List<SearchResponse.KeywordSearchDocument> searchByLocationAndCategoryAndMeter(
            @NonNull
            @ToolParam(description = "검색어, 예: \"서울 남산 타워\"")
            String location,

            @NonNull
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

            @NonNull
            @ToolParam(description = "검색 반경(미터 단위), 예: 20000")
            Integer meter
    ) {
        log.debug("[Tool] 검색 시작 - location='{}', category='{}', radius={}m", location, placeCategory, meter);

        // 1) 위치 검색
        Coordinates coords = resolveCoordinates(location);
        log.debug("[Tool] 위치 좌표 - x={}, y={}", coords.x, coords.y);

        // 2) 카테고리별 장소 검색 (페이징)
        List<SearchResponse.KeywordSearchDocument> results = fetchAllByCategory(placeCategory, coords.x, coords.y, meter);

        log.debug("[Tool] 총 {}개 장소 검색 완료", results.size());
        return results;
    }

    /**
     * 키워드로부터 좌표(x, y)를 조회합니다.
     */
    private Coordinates resolveCoordinates(String location) {
        SearchResponse response = kakaoClient
                .searchKeyword(location, 1, 1, null, null, null, "accuracy")
                .block();
        if (response == null || response.getDocuments().isEmpty()) {
            throw new IllegalStateException("위치 데이터를 찾을 수 없습니다: " + location);
        }
        SearchResponse.KeywordSearchDocument doc = response.getDocuments().get(0);
        return new Coordinates(
                Double.parseDouble(Objects.requireNonNull(doc.getX())),
                Double.parseDouble(Objects.requireNonNull(doc.getY()))
        );
    }

    /**
     * 지정된 좌표와 카테고리, 반경 내의 모든 장소를 페이징 조회합니다.
     */
    private List<SearchResponse.KeywordSearchDocument> fetchAllByCategory(
            String category,
            double x,
            double y,
            int radius
    ) {
        List<SearchResponse.KeywordSearchDocument> all = new ArrayList<>();
        int page = 1;

        while (true) {
            SearchResponse resp = kakaoClient
                    .searchCategory(category, x, y, page, 15, radius, "accuracy")
                    .block();

            if (resp == null || resp.getDocuments().isEmpty()) {
                break;
            }
            all.addAll(resp.getDocuments());

            // meta 정보를 활용해 마지막 페이지 확인 (end==true)
            if (resp.getMeta().is_end()) {
                break;
            }
            page++;
        }
        return all;
    }

    private record Coordinates(double x, double y) {
    }
}
