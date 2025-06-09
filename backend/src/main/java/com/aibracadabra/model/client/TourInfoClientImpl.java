package com.aibracadabra.model.client;

import com.aibracadabra.constant.ContentTypeId;
import com.aibracadabra.model.dto.client.TourInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class TourInfoClientImpl implements TourInfoClient {

    private final WebClient gwApiWebClient;
    @Value("${spring.web-client.gw-api-key}")
    private String apiKey;
    @Value("${spring.web-client.gw-mobile-app}")
    private String mobileApp;

    @Override
    public Mono<TourInfoResponse> sidoCode() {
        return gwApiWebClient.get()
                .uri(uri -> defaultParamsBuilder(uri)
                        .path("/areaCode2")
                        .build()
                )
                .retrieve()
                .bodyToMono(TourInfoResponse.class);
    }

    @Override
    public Mono<TourInfoResponse> gugunCode(String sidoCode) {
        return gwApiWebClient.get()
                .uri(uri -> defaultParamsBuilder(uri)
                        .path("/areaCode2")
                        .queryParam("areaCode", sidoCode)
                        .build()
                )
                .retrieve()
                .bodyToMono(TourInfoResponse.class);

    }

    @Override
    public Mono<TourInfoResponse> primaryCategoryCode(ContentTypeId contentTypeId) {
        return gwApiWebClient.get()
                .uri(uri -> defaultParamsBuilder(uri)
                        .path("/areaCode2")
                        .queryParam("contentTypeId", contentTypeId.getCode())
                        .build()
                )
                .retrieve()
                .bodyToMono(TourInfoResponse.class);
    }

    @Override
    public Mono<TourInfoResponse> secondaryCategoryCode(String primaryCategoryCode) {
        return gwApiWebClient.get()
                .uri(uri -> defaultParamsBuilder(uri)
                        .path("/areaCode2")
                        .queryParam("cat1", primaryCategoryCode)
                        .build()
                )
                .retrieve()
                .bodyToMono(TourInfoResponse.class);
    }

    @Override
    public Mono<TourInfoResponse> tertiaryCategoryCode(String primaryCategoryCode, String secondaryCategoryCode) {
        return gwApiWebClient.get()
                .uri(uri -> defaultParamsBuilder(uri)
                        .path("/areaCode2")
                        .queryParam("cat1", primaryCategoryCode)
                        .queryParam("cat2", secondaryCategoryCode)
                        .build()
                )
                .retrieve()
                .bodyToMono(TourInfoResponse.class);
    }

    @Override
    public Mono<TourInfoResponse> locationBasedList(
            String mapX,
            String mapY,
            Integer pageNo,
            String radius,
            String contentTypeId,
            String primaryCategoryCode,
            String secondaryCategoryCode) {

        return gwApiWebClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = defaultParamsBuilder(uriBuilder)
                            .path("/locationBasedList2")
                            .queryParam("mapX", mapX)
                            .queryParam("mapY", mapY)
                            .queryParam("pageNo", pageNo)
                            .queryParam("radius", radius);

                    Optional.ofNullable(contentTypeId)
                            .ifPresent(ct -> builder.queryParam("contentTypeId", ct));
                    Optional.ofNullable(primaryCategoryCode)
                            .ifPresent(pc -> builder.queryParam("primaryCategoryCode", pc));
                    Optional.ofNullable(secondaryCategoryCode)
                            .ifPresent(sc -> builder.queryParam("secondaryCategoryCode", sc));

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(TourInfoResponse.class);
    }

  @Override
  public Mono<TourInfoResponse> detailCommon(String contentId) {
    return gwApiWebClient.get()
      .uri(uriBuilder -> defaultParamsBuilder(uriBuilder)
        .path("/detailCommon2")
        .queryParam("contentId", contentId)
        .build()
      )
      .retrieve()
      .bodyToMono(TourInfoResponse.class);
  }


  private UriBuilder defaultParamsBuilder(UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("numOfRows", 100)
                .queryParam("MobileOS", "WEB")
                .queryParam("MobileApp", mobileApp)
                .queryParam("_type", "json")
                .queryParam("serviceKey", apiKey);
    }
}
