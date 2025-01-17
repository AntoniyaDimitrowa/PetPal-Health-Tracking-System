package com.example.petpal.business.impl;

import com.example.petpal.business.domain.WeatherApiResponse;

public class TestWeatherService extends WeatherServiceImpl {

    private final WeatherApiResponse mockResponse;

    public TestWeatherService(WeatherApiResponse mockResponse) {
        this.mockResponse = mockResponse;
    }

    @Override
    protected WeatherApiResponse fetchWeatherData(String url) {
        return mockResponse; // Return mock data instead of making an API call.
    }
}
