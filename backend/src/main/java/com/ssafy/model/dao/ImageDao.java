package com.ssafy.model.dao;

import com.ssafy.model.dto.domain.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {
	void insertImage(Image image);

	void updateImage(Image image);

	Image getImageById(int ino);

	void deleteImage(int ino);
}
