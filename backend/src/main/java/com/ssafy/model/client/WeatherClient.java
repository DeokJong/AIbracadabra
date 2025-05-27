package com.ssafy.model.client;

import com.ssafy.model.dto.client.WeatherInfo;

public interface WeatherClient {
	
	public WeatherInfo getWeather(double lat, double lon);

}
