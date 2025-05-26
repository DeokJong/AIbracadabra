package com.ssafy.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.model.dto.domain.HotPlace;

public interface HotPlaceService {
	
	void registHotPlace(HotPlace hotPlace, MultipartFile file);
	
	List<HotPlace> findByLocation(String mapX, String mapY, String meter);
	
	HotPlace findByHno(Integer hno);

	void update(HotPlace hotPlace, MultipartFile file);

	void deleteByHno(Integer hno, Integer mno);
} 
