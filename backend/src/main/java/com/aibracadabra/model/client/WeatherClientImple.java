package com.aibracadabra.model.client;

import com.aibracadabra.model.dto.client.OpenWeatherResponse;
import com.aibracadabra.model.dto.client.WeatherInfo;
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
//System("""
//        # 날씨 조회 도우미 가이드
//        이전 `recommendChatClient` 호출에서 반환된 `mapX`와 `mapY` 값을 사용해 아래 형식으로 날씨 툴을 호출하세요.
//
//        `tool`: "getWeatherByLocation"
//        `parameters`: {
//          "lat": <mapY 값>,   // 위도
//          "lon": <mapX 값>    // 경도
//        }
//
//        예시 JSON:
//        {
//          "tool": "getWeatherByLocation",
//          "parameters": {{ "lat": 37.5665, "lon": 126.9780 }}
//        }











