package com.ssafy.model.dto.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Route {
	private int contentid;
	private String title;
	private String address;
	private double x;
	private double y;

}
