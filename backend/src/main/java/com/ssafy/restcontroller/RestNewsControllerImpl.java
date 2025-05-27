package com.ssafy.restcontroller;

import com.ssafy.model.dto.domain.News;
import com.ssafy.model.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RestNewsController 인터페이스를 구현한 클래스
 */
@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class RestNewsControllerImpl implements RestNewsController {

    private final NewsService newsService;

    /**
     * 인터페이스에 정의된 newsList() 메서드를 구현.
     * service에서 모든 뉴스를 조회하여 HTTP 200으로 반환.
     */
    @Override
    public ResponseEntity<?> newsList() {
        List<News> list = newsService.selectAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<List<News>> newsBySido(int sidoCode) {
        List<News> list = newsService.selectBySidoCode(sidoCode);
        return ResponseEntity.ok(list);
    }
}
