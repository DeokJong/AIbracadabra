package com.ssafy.model.service;

import com.ssafy.model.dto.domain.Image;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	/**
	 * 새로운 이미지를 저장하고, 저장된 Image 객체(ino 포함)를 반환합니다.
	 *
	 * @param file 저장할 이미지
	 * @return 저장된 이미지 객체
	 */
	Image createImage(MultipartFile file);

	/**
	 * 특정 ino에 해당하는 이미지를 조회합니다.
	 *
	 * @param ino 조회할 이미지 ID
	 * @return 이미지 도메인 객체
	 */
	Resource getImageById(int ino);

	/**
	 * 기존 이미지 정보를 업데이트합니다.
	 *
	 * @param image 업데이트할 이미지 도메인 객체
	 */
	void updateImage(Image image);

	/**
	 * 특정 ino에 해당하는 이미지를 삭제합니다.
	 *
	 * @param ino 삭제할 이미지 ID
	 */
	void deleteImage(int ino);
}
