package com.aibracadabra.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aibracadabra.exception.RecordNotFoundException;
import com.aibracadabra.model.dao.NewsDao;
import com.aibracadabra.model.dto.domain.News;
import com.aibracadabra.model.service.NewsService;

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
