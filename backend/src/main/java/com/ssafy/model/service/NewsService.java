package com.ssafy.model.service;

import java.util.List;

import com.ssafy.model.dto.domain.News;

public interface NewsService {
	public List<News> selectAll();
	public List<News> selectBySidoCode(int sidoCode);

}
