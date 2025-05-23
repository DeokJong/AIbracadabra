package com.ssafy.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.exception.RecordNotFoundException;
import com.ssafy.model.dao.NewsDao;
import com.ssafy.model.dto.domain.News;
import com.ssafy.model.service.NewsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
	
	private final NewsDao newsDao;

	@Override
	public List<News> selectAll() {
		List<News> list = newsDao.selectAll();
		
		if(list ==null) {
			throw new RecordNotFoundException("조회된 뉴스 정보가 없습니다.");
		} 
		
		return list;
	}

	@Override
	public List<News> selectBySidoCode(int sidoCode) {
		List<News> list = newsDao.selectBySidoCode(sidoCode);
		
		if(list ==null) {
			throw new RecordNotFoundException("조회된 뉴스 정보가 없습니다.");
		} 
		
		return list;
	}

}
