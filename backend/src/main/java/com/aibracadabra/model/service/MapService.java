package com.aibracadabra.model.service;

import com.aibracadabra.model.dto.client.AddressSearchResponse;
import com.aibracadabra.model.dto.client.SearchResponse;
import com.aibracadabra.model.dto.domain.Document;
import reactor.core.publisher.Mono;

public interface MapService {
  /**
   * 카카오 API 위치 기반 검색
   *
   * @param query 위치 검색어
   * @param page  페이지 번호
   * @return 결과값
   */
  AddressSearchResponse searchAddress(String query, int page);

  /**
   * 카카오 API 키워드 기반검색
   *
   * @param query 검색 키워드
   * @param page  페이지 번호
   * @return 결과값
   */
  SearchResponse searchByKeyword(String query, int page);

  /**
   * 카카오 API 위치 기반 전용 Document 객체로 리턴
   *
   * @param query  위치 검색어
   * @param pageNo 페이지 번호
   * @return 결과값
   */
  Mono<Document> searchAddress(String query, Integer pageNo);

  default Mono<Document> searchContent(String mapX, String mapY, String contentTypeId) {
    return searchContent(mapX, mapY, contentTypeId, 1, "20000");
  }

  default Mono<Document> searchContent(String mapX, String mapY, String contentTypeId, Integer pageNo) {
    return searchContent(mapX, mapY, contentTypeId, pageNo, "20000");
  }

  Mono<Document> searchContent(String mapX, String mapY, String contentTypeId, Integer pageNo, String radius);

  Mono<Document> searchContentDetail(String contentId);
}
