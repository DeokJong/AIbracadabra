package com.aibracadabra.model.dto.client;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherInfo {
    private double temperature;
    private String description;
    private LocalTime sunrise;
    private LocalTime sunset;
}
