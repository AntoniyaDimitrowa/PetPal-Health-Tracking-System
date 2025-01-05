package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherConditions {
    private double temperature;
    private int humidity;
    private String description;

    public boolean isHot() {
        return temperature > 30; // Example threshold for "hot"
    }

    public boolean isCold() {
        return temperature < 10; // Example threshold for "cold"
    }
}
