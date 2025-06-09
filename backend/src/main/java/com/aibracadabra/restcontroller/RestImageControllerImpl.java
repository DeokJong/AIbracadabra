package com.aibracadabra.restcontroller;

import java.net.URLConnection;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aibracadabra.model.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
class RestImageControllerImpl implements ResponseEntityHelper, RestImageController {

	private final ImageService imageService;

	@Override
	public ResponseEntity<?> serveImage(@PathVariable Integer ino) {
		Resource img = imageService.getImageById(ino);
		
		String filename = img.getFilename();
		String contentType = URLConnection.guessContentTypeFromName(filename);
		
		if(contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(img);
	}
}
