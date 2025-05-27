package com.ssafy.model.client;

import com.ssafy.model.dto.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClientImpl implements KakaoClient {
  private final WebClient kakaoApiWebClient;

  /**
   * 주소 → 좌표 변환
   */
  @Override
  public Mono<AddressSearchResponse> searchAddress(String query, String analyzeType, Integer page, Integer size) {
    return kakaoApiWebClient.get()
            .uri(uri -> uri
                    .path("/search/address.json")
                    .queryParam("query", query)
                    .queryParam("analyze_type", analyzeType)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
            .retrieve()
            .bodyToMono(AddressSearchResponse.class);
  }

  /**
   * 좌표 → 행정구역정보 변환
   */
  @Override
  public Mono<RegionCodeResponse> coord2RegionCode(double x, double y, String inputCoord, String outputCoord) {
    return kakaoApiWebClient.get()
            .uri(uri -> uri
                    .path("/geo/coord2regioncode.json")
                    .queryParam("x", x)
                    .queryParam("y", y)
                    .queryParam("input_coord", inputCoord)
                    .queryParam("output_coord", outputCoord)
                    .build())
            .retrieve()
            .bodyToMono(RegionCodeResponse.class);
  }

  /**
   * 좌표 → 주소 변환
   */
  @Override
  public Mono<Coord2AddressResponse> coord2Address(double x, double y, String inputCoord) {
    return kakaoApiWebClient.get()
            .uri(uri -> uri
                    .path("/geo/coord2address.json")
                    .queryParam("x", x)
                    .queryParam("y", y)
                    .queryParam("input_coord", inputCoord)
                    .build())
            .retrieve()
            .bodyToMono(Coord2AddressResponse.class);
  }

  /**
   * 좌표계 변환
   */
  @Override
  public Mono<TransCoordResponse> transCoord(double x, double y, String inputCoord, String outputCoord) {
    return kakaoApiWebClient.get()
            .uri(uri -> uri
                    .path("/geo/transcoord.json")
                    .queryParam("x", x)
                    .queryParam("y", y)
                    .queryParam("input_coord", inputCoord)
                    .queryParam("output_coord", outputCoord)
                    .build())
            .retrieve()
            .bodyToMono(TransCoordResponse.class);
  }

  /**
   * 키워드로 장소 검색
   */
  @Override
  public Mono<SearchResponse> searchKeyword(String query, Integer page, Integer size, Double x, Double y, Integer radius, String sort) {
    return kakaoApiWebClient.get()
            .uri(uri -> {
              var b = uri.path("/search/keyword.json")
                      .queryParam("query", query)
                      .queryParam("page", page)
                      .queryParam("size", size)
                      .queryParam("sort", sort);
              if (x != null && y != null) {
                b = b.queryParam("x", x)
                        .queryParam("y", y)
                        .queryParam("radius", radius);
              }
              return b.build();
            })
            .retrieve()
            .bodyToMono(SearchResponse.class);
  }

  /**
   * 카테고리로 장소 검색
   */
  @Override
  public Mono<SearchResponse> searchCategory(String categoryGroupCode, Double x, Double y, Integer page, Integer size, Integer radius, String sort) {
    return kakaoApiWebClient.get()
            .uri(uri -> {
              var b = uri.path("/search/category.json")
                      .queryParam("category_group_code", categoryGroupCode)
                      .queryParam("page", page)
                      .queryParam("size", size)
                      .queryParam("sort", sort);
              if (x != null && y != null) {
                b = b.queryParam("x", x)
                        .queryParam("y", y)
                        .queryParam("radius", radius);
              }
              return b.build();
            })
            .retrieve()
            .bodyToMono(SearchResponse.class);
  }

}
