package com.example.petpal.business.impl;

import com.example.petpal.business.IWeatherService;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.WeatherApiResponse;
import com.example.petpal.business.domain.WeatherConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements IWeatherService {

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Override
    public WeatherConditions getCurrentConditions(User user) {
        String city = user.getAddress().split(", ")[0];
        String url = String.format("%s?q=%s&appid=%s&units=metric", weatherApiUrl, city, weatherApiKey);

        RestTemplate restTemplate = new RestTemplate();
        WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);
        if (response == null) {
            throw new IllegalStateException("Failed to fetch weather data");
        }

        return new WeatherConditions(
                response.getMain().getTemp(),
                response.getMain().getHumidity(),
                response.getWeather().get(0).getDescription()
        );
    }
}
