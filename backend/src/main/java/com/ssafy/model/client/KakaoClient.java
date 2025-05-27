package com.ssafy.model.client;

import com.ssafy.model.dto.client.*;
import reactor.core.publisher.Mono;

public interface KakaoClient {
  Mono<AddressSearchResponse> searchAddress(String query, String analyzeType, Integer page, Integer size);

  Mono<RegionCodeResponse> coord2RegionCode(double x, double y, String inputCoord, String outputCoord);

  Mono<Coord2AddressResponse> coord2Address(double x, double y, String inputCoord);

  Mono<TransCoordResponse> transCoord(double x, double y, String inputCoord, String outputCoord);

  default Mono<SearchResponse> searchKeyword(String query, Integer page, Integer size) {
    return searchKeyword(query, page, size, null, null, 20000, "accuracy");
  }

  Mono<SearchResponse> searchKeyword(String query, Integer page, Integer size, Double x, Double y, Integer radius, String sort);

  Mono<SearchResponse> searchCategory(String categoryGroupCode, Double x, Double y, Integer page, Integer size, Integer radius, String sort);
}
