package com.ssafy.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.model.dto.domain.HotPlace;

public interface HotPlaceService {
	
	void registerHotPlace(HotPlace hotPlace, MultipartFile file);
	
	List<HotPlace> findByLocation(String mapX, String mapY, String meter);
	
	HotPlace findByHno(Integer hno);

	void update(HotPlace hotPlace, MultipartFile file);

	void deleteByHno(Integer hno, Integer mno);

	void likeHotPlace(int mno, int hno);

	void unlikeHotPlace(int mno, int hno);

	List<Integer> findLikedHotPlaceIdsByMember(int mno);
}
