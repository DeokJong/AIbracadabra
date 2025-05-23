package com.ssafy.model.service.impl;

import com.ssafy.model.client.KakaoClient;
import com.ssafy.model.client.TourInfoClient;
import com.ssafy.model.dto.client.AddressSearchResponse;
import com.ssafy.model.dto.client.SearchResponse;
import com.ssafy.model.dto.domain.Document;
import com.ssafy.model.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
	private final KakaoClient kakaoClient;
	private final TourInfoClient tourInfoClient;

	@Override
	public AddressSearchResponse searchAddress(String query, int page) {
		return kakaoClient.searchAddress(query, "similar", page, 15).block();
	}

	@Override
	public SearchResponse getSearchByQuery(String query, int page) {
		return kakaoClient.searchKeyword(query, page, 15).block();
	}

	@Override
	public Mono<Document> searchAddress(String query, Integer pageNo) {
		return kakaoClient
			.searchAddress(query, "similar", pageNo, 15)    // Mono<AddressSearchResponse>
			.map(result -> Document.builder()
				.meta(Document.Meta.builder()
					.isEnd(result.getMeta().is_end())
					.pageNo(pageNo)
					.numOfRows(result.getMeta().getTotal_count())
					.build()
				)
				.documents(
					result.getDocuments().stream()
						.map(raw -> Document.Item.builder()
							.mapX(raw.getX())
							.mapY(raw.getY())
							.address(raw.getAddress_name())
							.build()
						)
						.toList()
				)
				.build()
			);
	}

	@Override
	public Mono<Document> searchContent(String mapX, String mapY, String contentTypeId, Integer pageNo, String radius) {
		return tourInfoClient.locationBasedList(mapX, mapY, pageNo, radius, contentTypeId)
			.map(result -> {
				var body = result.getResponse().getBody();
				return Document.builder()
					.meta(Document.Meta.builder()
						.totalCount(body.getTotalCount())
						.numOfRows(body.getNumOfRows())
						.pageNo(pageNo)
						.isEnd(body.getNumOfRows() < 100 || (long) pageNo * body.getNumOfRows() >= body.getTotalCount())
						.build())
					.documents(result.getResponse().getBody().getItems().getItem().stream().map(
						raw -> Document.Item.builder()
							.mapY(raw.getMapy())
							.mapX(raw.getMapx())
							.title(raw.getTitle())
							.tel(raw.getTel())
							.firstImage(raw.getFirstimage())
							.address(raw.getAddr1() + raw.getAddr2())
							.contentsTypeId(contentTypeId)
							.contentId(raw.getContentid())
							.build()
					).toList())
					.build();
			});
	}

	@Override
	public Mono<Document> searchContentDetail(String contentId) {
		return tourInfoClient.detailCommon(contentId)
			.map(result -> {
				var body = result.getResponse().getBody();
				return Document.builder()
					.meta(Document.Meta.builder()
						.totalCount(body.getTotalCount())
						.numOfRows(body.getNumOfRows())
						.pageNo(1)
						.isEnd(true)
						.build())
					.documents(result.getResponse().getBody().getItems().getItem().stream().map(
						raw -> Document.Item.builder()
							.mapY(raw.getMapy())
							.mapX(raw.getMapx())
							.title(raw.getTitle())
							.tel(raw.getTel())
							.firstImage(raw.getFirstimage())
							.address(raw.getAddr1() + raw.getAddr2())
							.contentsTypeId(raw.getContenttypeid())
							.contentId(raw.getContentid())
							.overview(raw.getOverview())
							.homepage(raw.getHomepage())
							.build()
					).toList())
					.build();
			});
	}

}
