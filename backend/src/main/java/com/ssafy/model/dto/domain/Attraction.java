package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attraction {
	@JsonProperty(access = Access.READ_ONLY)
	int no;
	int content_id;
	String title;
	int content_type_id;
	int area_code;
	int si_gun_gu_code;
	String first_image1;
	String first_image2;
	int map_level;
	int latitude;
	int longitude;
	int tel;
	String addr1;
	String addr2;
	String homepage;
	String overview;
}
