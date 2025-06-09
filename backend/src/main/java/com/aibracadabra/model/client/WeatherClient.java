package com.aibracadabra.model.client;

import com.aibracadabra.model.dto.client.WeatherInfo;

public interface WeatherClient {
	
	public WeatherInfo getWeather(double lat, double lon);

}
