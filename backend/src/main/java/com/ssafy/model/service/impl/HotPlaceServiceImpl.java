package com.ssafy.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.model.dao.HotPlaceDao;
import com.ssafy.model.dto.domain.HotPlace;
import com.ssafy.model.dto.domain.Image;
import com.ssafy.model.service.HotPlaceService;
import com.ssafy.model.service.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class HotPlaceServiceImpl implements HotPlaceService {

	private final ImageService imageService;

	private final HotPlaceDao hotPlaceDao;

	@Override
	public void registHotPlace(HotPlace hotPlace, MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			Image image = imageService.createImage(file);
			hotPlace.setIno(image.getIno());
		}
		hotPlaceDao.save(hotPlace);
	}

	@Override
	public List<HotPlace> findByLocation(String mapX, String mapY, String meter) {
		List<HotPlace> result = hotPlaceDao.findByCoordinates(Double.parseDouble(mapX), Double.parseDouble(mapY), Integer.parseInt(meter));
		for (HotPlace hotPlace : result) {
			if (hotPlace.getIno() != null) {
				hotPlace.setImageUrl("http://localhost:8080/api/v1/images/" + hotPlace.getIno());
			}
		}
		return result;
	}

	@Override
	public HotPlace findByHno(Integer hno) {
		var result = hotPlaceDao.findById(hno);
		if (result.getIno() != null) {
			result.setImageUrl("http://localhost:8080/api/v1/images/" + result.getIno());
		}
		return result;
	}

	@Override
	public void update(HotPlace hotPlace, MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			Image image = imageService.createImage(file);
			hotPlace.setIno(image.getIno());
		}
		hotPlaceDao.update(hotPlace);
	}

	@Override
	public void deleteByHno(Integer hno, Integer mno) {
		hotPlaceDao.delete(hno,mno);
	}
}
