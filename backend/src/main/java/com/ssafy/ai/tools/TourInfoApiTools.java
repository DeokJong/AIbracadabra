package com.ssafy.ai.tools;

import com.ssafy.model.dto.domain.Document;
import com.ssafy.model.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourInfoApiTools {
  private final MapService mapService;

  @Tool(
          description = """
                  검색 키워드 기반 위치 좌표값 출력
                  입력값:
                    전주 맛집, 장덕동, 광주 수완지구

                  출력값:
                  Coordinates 객체
                  Coordinates : {{
                    mapX: 경도
                    mapY: 위도
                    addressName: 입력값에 대한 자세한 주솟값
                  }}

                  최대한 입력값과 Coordinates.addressName 값이 일치 하는 것들로 선택
                  """,
          name = "searchLocationByKeyword"
  )
  public List<Coordinates> searchLocationByKeyword(@ToolParam(description = "위치를 검색할 키워드") String keyword) {
    log.debug("==> Parameters: {}(keyword)", keyword);
    var result = mapService.searchByKeyword(keyword, 1)
            .getDocuments()
            .stream()
            .map(e -> new Coordinates(e.getX(), e.getY(), e.getAddress_name()))
            .toList();
    for (var loc : result) {
      log.trace("<==\taddressName: {}", loc.addressName);
    }
    log.debug("<== Total: {}", result.size());
    return result;
  }

  @Tool(
          description = """
                  위치 좌표값 {mapX}, {mapY} 중심으로 {meter}반경으로 {contentTypeID}에 속하는 데이터를 조회한다.
                                    
                  contentTypeID = [
                    { "code": "12", "description": "관광지" },
                    { "code": "14", "description": "문화시설" },
                    { "code": "15", "description": "축제공연행사" },
                    { "code": "25", "description": "여행코스" },
                    { "code": "28", "description": "레포츠" },
                    { "code": "32", "description": "숙박" },
                    { "code": "38", "description": "쇼핑" },
                    { "code": "39", "description": "음식점" }
                  ]
                  컨텐츠 타입은 위와같은 `code` 값만 받는 것을 인지해라.
                                    
                  위치 좌표를 얻기 위해선 먼저 `searchLocationByKeyword` 를 이용해서 얻어라
                  """
  )
  public List<Document.Item> searchContentByLocation(@ToolParam(description = "맵의 중심 좌표 값에 대한 경도") String mapX,
                                                     @ToolParam(description = "맵의 중심 좌표 값에 대한 위도") String mapY,
                                                     @ToolParam(description = "검색할 컨텐츠 타입") String contentTypeID,
                                                     @ToolParam(description = "검색할 반경(m 단위)") String meter) {
    log.debug("==> Parameters: {}(mapX), {}(mapY), {}(contentTypeId), {}(meter)", mapX, mapY, contentTypeID, meter);
    var result = Objects.requireNonNull(mapService.searchContent(mapX, mapY, contentTypeID, 1, meter).block()).getDocuments();
    log.debug("<== Total: {}", result.size());
    return result;
  }

  public record Coordinates(String mapX, String mapY, String addressName) {
  }
}
