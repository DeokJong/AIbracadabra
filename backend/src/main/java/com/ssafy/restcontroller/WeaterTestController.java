package com.ssafy.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.ai.tools.TourInfoApiTools;
import com.ssafy.model.dto.client.WeatherInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WeaterTestController {
	private final TourInfoApiTools tourInfoApiTools;

    /**
     * 예시 호출:
     *  GET http://localhost:8080/api/test/weather?lat=37.5665&lon=126.9780
     */
    @GetMapping("/api/test/weather")
    public WeatherInfo testWeather(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon
    ) {
        return tourInfoApiTools.getWeatherByLocation(lat, lon);
    }
}
