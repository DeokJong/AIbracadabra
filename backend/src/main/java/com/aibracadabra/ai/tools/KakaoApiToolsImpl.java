package com.aibracadabra.ai.tools;

import com.aibracadabra.model.client.KakaoClientImpl;
import com.aibracadabra.model.dto.client.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class KakaoApiToolsImpl implements KakaoApiTools {
  private final KakaoClientImpl kakaoClient;


  @Override
  public List<SearchResponse.KeywordSearchDocument> searchByLocationAndCategoryAndMeter(String location, String placeCategory, Integer meter) {
    log.debug("[Tool] 검색 시작 - location='{}', category='{}', radius={}m", location, placeCategory, meter);

    // 1) 위치 검색
    Coordinates coords = resolveCoordinates(location);

    // 2) 카테고리별 장소 검색 (페이징)
    return fetchAllByCategory(placeCategory, coords.x, coords.y, meter);
  }

  /**
   * 카테고리, 좌표, 반경 기준으로 검색 및 페이징
   * @param category 카테고리 코드
   * @param x x좌표
   * @param y y좌표
   * @param radius 반경
   * @return 검색 결과를 페이징 한 값
   */
  public List<SearchResponse.KeywordSearchDocument> fetchAllByCategory(String category, double x, double y, int radius) {
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


  /**
   * 위치 키워드에서 x, y 좌표만 추출
   * @param location - 위치 키워드
   * @return - x,y 좌표가 담긴 VO
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

  private record Coordinates(double x, double y) {
  }

}
