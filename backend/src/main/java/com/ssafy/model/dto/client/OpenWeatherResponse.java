package com.ssafy.model.dto.client;

import java.util.List;

import lombok.Data;

@Data
public class OpenWeatherResponse {
	private List<Weather> weather;
	private Main main;
	private Sys sys;
	
	@Data
	public static class Weather {
		private String description;
	}
	
	@Data
	public static class Main{
		private double temp;
		private int humidity;
	}
	
	@Data
	public static class Sys {
		private long sunrise;
		private long sunset;
	}
	
}
