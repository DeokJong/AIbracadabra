package com.aibracadabra.model.service;

import java.util.List;

import com.aibracadabra.model.dto.domain.News;

public interface NewsService {
	public List<News> selectAll();
	public List<News> selectBySidoCode(int sidoCode);

}
