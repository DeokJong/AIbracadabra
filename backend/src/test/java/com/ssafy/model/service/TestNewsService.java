package com.ssafy.model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.ssafy.model.dto.domain.News;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TestNewsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

    @Autowired
    private NewsService newsService;

    @BeforeEach
    void setup() {
    	jdbcTemplate.update("DELETE FROM news");
    	jdbcTemplate.update("""
				INSERT INTO news(title, publish_at, url_sido_code, summary, url)
				VALUES (?, ?, ?, ?, ?)
				""", "testTitle", // name
				"2025-05-11 14:02:00", // email
				"4", // password
				"요약한내용",
				"test.com"// role (NOT NULL)
		);
    	
    }
    @Test
    @DisplayName("newsSelectAll(): 크롤링한 뉴스 정보가져오기 테스트")
    void newsSeletAll_Test() {
    	List<News> list = newsService.selectAll();
    	assertEquals(list.size(), 1);
    }
    @Test
    @DisplayName("newsseletAll_Throw(): 가져온 뉴스 리스트가 비었으면 예외를 던져야함")
    void selectAll_ThrowsWhenEmpty() {
        jdbcTemplate.update("DELETE FROM news");
    	List<News> list = newsService.selectAll();

    	assertEquals(list.size(), 0);

    }

    
    
    
    
    
}
