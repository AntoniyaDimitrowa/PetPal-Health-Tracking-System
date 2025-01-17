package com.example.petpal.business.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.WeatherApiResponse;
import com.example.petpal.business.domain.WeatherConditions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class WeatherServiceImplTest {

    private TestWeatherService weatherService;
    private User user;
    private WeatherApiResponse weatherApiResponse;

    @BeforeEach
    void setUp() {
        user = User.builder().address("New York, NY").build();

        // Sample weather API response
        weatherApiResponse = new WeatherApiResponse();
        WeatherApiResponse.Main main = new WeatherApiResponse.Main();
        main.setTemp(22.5);
        main.setHumidity(65);
        weatherApiResponse.setMain(main);

        WeatherApiResponse.Weather weather = new WeatherApiResponse.Weather();
        weather.setDescription("clear sky");
        weatherApiResponse.setWeather(List.of(weather));

        // Use the test helper class with the mock response
        weatherService = new TestWeatherService(weatherApiResponse);
    }

    @Test
    void getCurrentConditions_shouldReturnWeatherConditions() {
        // Act
        WeatherConditions result = weatherService.getCurrentConditions(user);

        // Assert
        assertNotNull(result);
        assertEquals(22.5, result.getTemperature());
        assertEquals(65, result.getHumidity());
        assertEquals("clear sky", result.getDescription());
    }

    @Test
    void getCurrentConditions_shouldThrowExceptionWhenResponseIsNull() {
        // Arrange
        weatherService = new TestWeatherService(null); // Simulate null API response

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> weatherService.getCurrentConditions(user));
        assertEquals("Failed to fetch weather data", thrown.getMessage());
    }

    @Test
    void getCurrentConditions_shouldHandleInvalidCity() {
        // Arrange: Simulate a valid API response for an invalid city
        User invalidUser = User.builder().address("Invalid City, XX").build();

        // Act
        WeatherConditions result = weatherService.getCurrentConditions(invalidUser);

        // Assert
        assertNotNull(result); // Ensure the response is handled gracefully
    }

    @Test
    void getCurrentConditions_shouldHandleEmptyCityAddress() {
        // Arrange: Simulate a user with an empty address
        User emptyUser = User.builder().address("").build();

        // Act
        WeatherConditions result = weatherService.getCurrentConditions(emptyUser);

        // Assert
        assertNotNull(result); // Ensure the response is handled gracefully
    }
}
