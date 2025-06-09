package com.aibracadabra.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotPlace {
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer hno;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer mno;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer ino;
	private String overview;
	private String title;

	private String mapX;
	private String mapY;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String imageUrl;

	@JsonIgnore
	private Image image;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime createdAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime updatedAt;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer likeCount;
}
