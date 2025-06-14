package com.aibracadabra.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aibracadabra.ai.tools.TourInfoApiTools;
import com.aibracadabra.model.dto.client.WeatherInfo;

@RestController
@RequestMapping("/test")
public class WeaterTestController {

    private final TourInfoApiTools tourInfoApiTools;

    public WeaterTestController(TourInfoApiTools tourInfoApiTools) {
        this.tourInfoApiTools = tourInfoApiTools;
    }

    @GetMapping("/weather")
    public WeatherInfo testWeather(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        // double → String 변환
        String sLat = String.valueOf(lat);
        String sLon = String.valueOf(lon);
        return tourInfoApiTools.getWeatherByLocation(sLat, sLon);
    }
}
