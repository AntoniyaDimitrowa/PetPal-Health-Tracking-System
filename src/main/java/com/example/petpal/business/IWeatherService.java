package com.example.petpal.business;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.WeatherConditions;

public interface IWeatherService {
    WeatherConditions getCurrentConditions(User user);
}