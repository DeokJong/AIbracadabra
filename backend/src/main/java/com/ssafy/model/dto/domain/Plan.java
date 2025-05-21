package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Plan {
	@JsonProperty(access = Access.READ_ONLY)
	private int pno;
	private String title;
	private int days;
	private int scheduleCount;
	private int mno;

}
