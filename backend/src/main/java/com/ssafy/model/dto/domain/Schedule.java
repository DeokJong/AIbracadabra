package com.ssafy.model.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Schedule {
	@JsonProperty(access = Access.READ_ONLY)
	private int sno;
	private int order;
	private int pno;
	private int ano;

}
