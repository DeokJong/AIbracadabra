package com.ssafy.model.client;

import com.ssafy.constant.ContentTypeId;
import com.ssafy.model.dto.client.TourInfoResponse;
import reactor.core.publisher.Mono;

public interface TourInfoClient {
    /**
     * 시도 지역 관련 코드를 반환.
     *
     * @return 시도 지역 코드가 담긴 GW Response 객체.
     */
    Mono<TourInfoResponse> sidoCode();

    /**
     * 시도 지역 코드를 기반으로 구군 코드를 반환.
     *
     * @param sidoCode 시도 코드 <code>sidoCode()</code>를 이용해서 얻음.
     * @return 구군 지역 코드가 담긴 GW Response 객체.
     */
    Mono<TourInfoResponse> gugunCode(String sidoCode);

    /**
     * 컨텐츠 타입 기반 대분류 카테고리를 받환.
     *
     * @param contentTypeId 컨텐츠 타입.
     * @return 대분류 카테고리가 담긴 GW Response 객체.
     */
    Mono<TourInfoResponse> primaryCategoryCode(ContentTypeId contentTypeId);

    /**
     * 대분류 카테고리 기반 중분류 카테고리 반환.
     *
     * @param primaryCategoryCode 대분류 카테고리. <code>primaryCategoryCode()</code>를 이용.
     * @return 중분류 카테고리가 담긴 GW Response 객체.
     */
    Mono<TourInfoResponse> secondaryCategoryCode(String primaryCategoryCode);

    /**
     * 대분류 카테고리와 중분류 카테고리 기반 소분류 카테고리 반환.
     *
     * @param primaryCategoryCode   대분류 카테고리. <code>primaryCategoryCode()</code>를 이용
     * @param secondaryCategoryCode 중분류 카테고리. <code>secondaryCategoryCode()</code>를 이용
     * @return 소분류 카테고리가 담긴 GW Response 객체.
     */
    Mono<TourInfoResponse> tertiaryCategoryCode(String primaryCategoryCode, String secondaryCategoryCode);

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX WGS84 경도좌표
     * @param mapY WGS84 위도 좌표
     * @return 해당 위치 좌표 기준 20km 이내 모든 타입의 컨텐츠를 1페이지 조회
     */
    default Mono<TourInfoResponse> locationBasedList(String mapX, String mapY) {
        return locationBasedList(mapX, mapY, 1, "20000");
    }

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX   WGS84 경도좌표
     * @param mapY   WGS84 위도 좌표
     * @param pageNo 페이지 번호
     * @return 해당 위치 좌표 기준 20km 이내 모든 타입의 컨텐츠를 <code>pageNo</code> 페이지 조회
     */
    default Mono<TourInfoResponse> locationBasedList(String mapX, String mapY, Integer pageNo) {
        return locationBasedList(mapX, mapY, pageNo, "20000");
    }

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX   WGS84 경도좌표
     * @param mapY   WGS84 위도 좌표
     * @param pageNo 페이지 번호
     * @param radius 주변 반경(m 단위)
     * @return 해당 위치 좌표 기준 <code>radius</code> 이내 모든 타입의 컨텐츠를 <code>pageNo</code> 페이지 조회
     */
    default Mono<TourInfoResponse> locationBasedList(String mapX, String mapY, Integer pageNo, String radius) {
        return locationBasedList(mapX, mapY, pageNo, radius, null);
    }

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX          WGS84 경도좌표
     * @param mapY          WGS84 위도 좌표
     * @param pageNo        페이지 번호
     * @param radius        주변 반경(m 단위)
     * @param contentTypeId 컨텐츠 타입
     * @return 해당 위치 좌표 기준 <code>radius</code> 이내 <code>contentTypeId</code> 타입의 컨텐츠를 <code>pageNo</code> 페이지 조회
     */
    default Mono<TourInfoResponse> locationBasedList(String mapX, String mapY, Integer pageNo, String radius, String contentTypeId) {
        return locationBasedList(mapX, mapY, pageNo, radius, contentTypeId, null);
    }

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX                WGS84 경도좌표
     * @param mapY                WGS84 위도 좌표
     * @param pageNo              페이지 번호
     * @param radius              주변 반경(m 단위)
     * @param contentTypeId       컨텐츠 타입
     * @param primaryCategoryCode 대분류 카테고리
     * @return 해당 위치 좌표 기준 <code>radius</code> 이내 <code>contentTypeId</code> 타입의 컨텐츠를 하위 카테고리에 맞게 <code>pageNo</code> 페이지 조회
     */
    default Mono<TourInfoResponse> locationBasedList(String mapX, String mapY, Integer pageNo, String radius, String contentTypeId, String primaryCategoryCode){
        return locationBasedList(mapX, mapY, pageNo, radius, contentTypeId, primaryCategoryCode, null);
    }

    /**
     * 위치 기반 기본 호출
     *
     * @param mapX                  WGS84 경도좌표
     * @param mapY                  WGS84 위도 좌표
     * @param pageNo                페이지 번호
     * @param radius                주변 반경(m 단위)
     * @param contentTypeId         컨텐츠 타입
     * @param primaryCategoryCode   대분류 카테고리
     * @param secondaryCategoryCode 중분류 카테고리
     * @return 해당 위치 좌표 기준 <code>radius</code> 이내 <code>contentTypeId</code> 타입의 컨텐츠를 하위 카테고리에 맞게 <code>pageNo</code> 페이지 조회
     */
    Mono<TourInfoResponse> locationBasedList(String mapX, String mapY, Integer pageNo, String radius, String contentTypeId, String primaryCategoryCode, String secondaryCategoryCode);

    /**
     * Content ID 기반 상세 정보 조회
     * @param contentId 컨텐트 ID
     * @return 조회 결과
     */
    Mono<TourInfoResponse> detailCommon(String contentId);
}
