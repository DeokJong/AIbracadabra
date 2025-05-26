package com.ssafy.model.client;

import com.ssafy.model.dto.client.OpenWeatherResponse;
import com.ssafy.model.dto.client.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class WeatherClientImple implements WeatherClient {
    private final RestTemplate restTemplate;

	
    @Value("${weather.openweathermap.url}")
    private String apiUrl;
    @Value("${weather.openweathermap.api-key}")
    private String apiKey;
    @Value("${weather.openweathermap.units}")
    private String units;
    @Value("${weather.openweathermap.lang}")
    private String lang;

	@Override
	public WeatherInfo getWeather(double lat, double lon) {
		String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=%s&lang=%s",
                apiUrl, lat, lon, apiKey, units, lang);

        OpenWeatherResponse resp = restTemplate
            .getForObject(url, OpenWeatherResponse.class);

        // 2) 응답에서 필요한 데이터 추출
        double temp = resp.getMain().getTemp();
        String desc = resp.getWeather().isEmpty()
                ? ""
                : resp.getWeather().get(0).getDescription();

        // 3) UNIX UTC(초) → 서울 LocalTime 변환
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        LocalTime sunrise = LocalTime.ofInstant(
            Instant.ofEpochSecond(resp.getSys().getSunrise()), seoul);
        LocalTime sunset = LocalTime.ofInstant(
            Instant.ofEpochSecond(resp.getSys().getSunset()), seoul);

        return new WeatherInfo(temp, desc, sunrise, sunset);
    }

}
