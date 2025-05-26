package com.ssafy.model.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
	private Integer ino;
	private String filename;
	private String contentType;
	private Long size;
	private String storagePath;
	private Date createdAt;
	private Date updatedAt;
}
