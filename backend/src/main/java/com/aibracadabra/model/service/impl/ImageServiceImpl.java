package com.aibracadabra.model.service.impl;

import com.aibracadabra.exception.RecordNotFoundException;
import com.aibracadabra.model.dao.ImageDao;
import com.aibracadabra.model.dto.domain.Image;
import com.aibracadabra.model.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 이미지 CRUD 및 파일 저장 서비스 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

	private static final List<String> ALLOWED =
		List.of(".jpg",".jpeg",".png",".gif",".webp",".bmp");
	private final ImageDao imageDao;

	@Value("${upload.path}")
	private String uploadDir;

	@Override
	public Image createImage(MultipartFile file) {

		String orig = file.getOriginalFilename();
		String ext = orig.substring(orig.lastIndexOf('.')).toLowerCase();
		if (!ALLOWED.contains(ext)) {
			log.debug("ContentType :{}", file.getContentType());
			throw new IllegalArgumentException("허용되지 않는 확장자");
		}

		String uuid = UUID.randomUUID() + ext;
		Path target = Paths.get(uploadDir, uuid);

		try {
			Files.createDirectories(target.getParent());
			file.transferTo(target.toFile());
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}

		Image image = Image.builder()
			.filename(orig)
			.contentType(file.getContentType())
			.size(file.getSize())
			.storagePath(target.toString())
			.build();
		imageDao.insertImage(image);
		return image;
	}

	@Override
	public Resource getImageById(int ino) {
		Image image = imageDao.getImageById(ino);
		Path file = Paths.get(image.getStoragePath());
		
		try {
			Resource imageResource = new UrlResource(file.toUri());
			if(!imageResource.exists() || !imageResource.isReadable()) {
				throw new RecordNotFoundException("이미지를 찾을 수 없습니다.");
			}
			return imageResource;
		} catch (MalformedURLException e) {
			throw new RecordNotFoundException("이미지를 찾을 수 없습니다.");
		}
	}

	@Override
	public void updateImage(Image image) {
		imageDao.updateImage(image);
	}

	@Override
	public void deleteImage(int ino) {
		imageDao.deleteImage(ino);
	}
}
