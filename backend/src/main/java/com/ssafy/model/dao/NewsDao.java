package com.ssafy.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.model.dto.domain.News;

@Mapper
public interface NewsDao {
	
	// 크롤링할 때 쓰이는 두 함수
	// 크롤링할 때 중복을 피하기위한 함수
	News selectByUrl(String url);
	
	// 실제 DB에 저장하기 위함 함수
    int insert(News news);
    
    // 웹에 보내기 위한 함수
    // 시도 코드로 가져오는 함수
    List<News> selectAll();

}
