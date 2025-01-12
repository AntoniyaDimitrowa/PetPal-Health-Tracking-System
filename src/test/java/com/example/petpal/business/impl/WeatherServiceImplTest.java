package com.example.petpal.business.impl;

import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.WeatherApiResponse;
import com.example.petpal.business.domain.WeatherConditions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class WeatherServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private User user;
    private WeatherApiResponse weatherApiResponse;


    private final String weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather";

    private final String weatherApiKey = "apiKey";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }

    @Test
    void getCurrentConditions_shouldReturnWeatherConditions() {
        // Arrange
        String city = user.getAddress().split(", ")[0];
        String url = String.format("%s?q=%s&appid=%s&units=metric", weatherApiUrl, city, weatherApiKey);
        when(restTemplate.getForObject(url, WeatherApiResponse.class)).thenReturn(weatherApiResponse);

        // Act
        WeatherConditions result = weatherService.getCurrentConditions(user);

        // Assert
        assertNotNull(result);
        assertEquals(22.5, result.getTemperature());
        assertEquals(65, result.getHumidity());
        assertEquals("clear sky", result.getDescription());
        verify(restTemplate).getForObject(url, WeatherApiResponse.class);
    }

    @Test
    void getCurrentConditions_shouldThrowExceptionWhenResponseIsNull() {
        // Arrange
        String url = "http://example.com?q=New York, NY&appid=testKey&units=metric";
        when(restTemplate.getForObject(url, WeatherApiResponse.class)).thenReturn(null);

        // Act & Assert
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> weatherService.getCurrentConditions(user));
        assertEquals("Failed to fetch weather data", thrown.getMessage());
        verify(restTemplate).getForObject(url, WeatherApiResponse.class);
    }

    @Test
    void getCurrentConditions_shouldHandleInvalidCity() {
        // Arrange: Simulate a scenario where the API returns a response for an invalid city
        User invalidUser = User.builder().address("Invalid City, XX").build();
        String url = "http://example.com?q=Invalid City, XX&appid=testKey&units=metric";
        when(restTemplate.getForObject(url, WeatherApiResponse.class)).thenReturn(weatherApiResponse);

        // Act
        WeatherConditions result = weatherService.getCurrentConditions(invalidUser);

        // Assert
        assertNotNull(result);
        verify(restTemplate).getForObject(url, WeatherApiResponse.class);
    }

    @Test
    void getCurrentConditions_shouldHandleEmptyCityAddress() {
        // Arrange: Simulate a case where user address is empty
        User emptyUser = User.builder().address("").build();
        String url = "http://example.com?q=&appid=testKey&units=metric";
        when(restTemplate.getForObject(url, WeatherApiResponse.class)).thenReturn(weatherApiResponse);

        // Act
        WeatherConditions result = weatherService.getCurrentConditions(emptyUser);

        // Assert
        assertNotNull(result);
        verify(restTemplate).getForObject(url, WeatherApiResponse.class);
    }

    @Test
    void getCurrentConditions_shouldHandleMalformedResponse() {
        // Arrange: Simulate a malformed response scenario
        String url = "http://example.com?q=New York, NY&appid=testKey&units=metric";
        when(restTemplate.getForObject(url, WeatherApiResponse.class)).thenThrow(new RuntimeException("Malformed response"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> weatherService.getCurrentConditions(user));
        assertEquals("Malformed response", thrown.getMessage());
        verify(restTemplate).getForObject(url, WeatherApiResponse.class);
    }
}
