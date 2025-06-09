package com.aibracadabra.restcontroller;

import com.aibracadabra.model.dto.domain.News;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 뉴스 관련 API를 정의하는 인터페이스
 */
@Tag(name = "News", description = "뉴스 API")
public interface RestNewsController {

    @Operation(
        summary = "뉴스 목록 조회",
        description = "모든 뉴스 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = News.class))
            )
        )
    })
    @GetMapping
    ResponseEntity<?> newsList();
    
    @Operation(
            summary = "지역별 뉴스 목록 조회",
            description = "주어진 sidoCode에 해당하는 뉴스 정보를 조회합니다."
        )
        @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = News.class))
                )
            ),
            @ApiResponse(responseCode = "404", description = "해당 지역의 뉴스가 존재하지 않습니다.")
        })
        @GetMapping("/{sidoCode}")
        ResponseEntity<List<News>> newsBySido(
            @PathVariable("sidoCode") int sidoCode
        );
}












