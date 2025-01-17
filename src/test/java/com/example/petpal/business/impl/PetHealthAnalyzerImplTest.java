package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.IWeatherService;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.*;
import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class PetHealthAnalyzerImplTest {

    @Mock
    private IHealthService healthService;

    @Mock
    private IWeatherService weatherService;

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private NotificationGenerator notificationGenerator;

    @Mock
    private INotificationRepository notificationRepository;

    @InjectMocks
    private PetHealthAnalyzerImpl petHealthAnalyzer;

    private static final Long PET_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long BREED_ID = 1L;
    private static final Long HEALTH_RECORD_ID = 1L;

    private static final Mood mood = Mood.builder().id(1L).name("Happy").emoji("😊").build();

    private static final Breed breed = Breed.builder()
            .id(BREED_ID)
            .name("Labrador")
            .description("Friendly, outgoing.")
            .normalMood(mood)
            .minimumExercisePerDay(1.5)
            .build();


    private static final Pet pet = Pet.builder()
            .id(PET_ID)
            .name("Buddy")
            .breed(breed)
            .gender(Gender.MALE)
            .birthdate(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime())
            .weight(25.5)
            .healthRecords(new ArrayList<>())
            .build();
    private static final User user = User.builder()
            .id(USER_ID)
            .name("John Doe")
            .email("john.doe@example.com")
            .password("password123")
            .role("USER")
            .memberSince(new java.util.Date())
            .address("1234 Main St, Hometown")
            .image("image_url")
            .pets(Optional.of(List.of(pet)))
            .build();



    private static final HealthRecord healthRecord = HealthRecord.builder()
            .id(HEALTH_RECORD_ID)
            .foodIntake(30.0)
            .waterIntake(20.0)
            .activityLevel(3)
            .mood(Mood.builder().name("Sad").build())
            .build();

    private static final BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder()
            .normalFoodIntake(50.0)
            .normalWaterIntake(60.0)
            .build();

    private static final WeatherConditions weatherConditions = WeatherConditions.builder()
            .temperature(32)
            .build();

    private static final HealthAnalysisResult healthAnalysisResult = HealthAnalysisResult.builder()
            .resultId(HEALTH_RECORD_ID)
            .date(new Date())
            .pet(pet)
            .message("Low food intake., Low water intake., Mood deviation detected., Low activity level.")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void analyzeHealthRecord_shouldAnalyzeHealthRecordAndGenerateNotification() throws InvalidPetException, InvalidUserException {
        int petAge = Calendar.getInstance().get(Calendar.YEAR) - 2015; // Adjust birth year accordingly
        double adjustedFoodIntake = 45.0;
        double adjustedWaterIntake = 48.0;

        // When
        List<String> anomalies = invokePrivateMethod("detectAnomalies", healthRecord, adjustedFoodIntake, adjustedWaterIntake, breed);

        // Given
        when(petRepository.getPet(PET_ID)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(PET_ID)).thenReturn(Optional.of(user));
        when(healthService.getHealthInfoForBreed(BREED_ID, petAge)).thenReturn(breedHealthInfo);
        when(weatherService.getCurrentConditions(user)).thenReturn(weatherConditions);
        when(healthService.getRecentRecords(PET_ID, 3)).thenReturn(new ArrayList<>());
        when(notificationGenerator.generateNotification(anyString())).thenReturn(String.join(", ", anomalies));
        // When
        HealthAnalysisResult result = petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord);

        // Then
        assertNotNull(result);
        assertEquals("Low food intake., Low water intake., Mood deviation detected., Low activity level.", result.getMessage());
        verify(petRepository, times(1)).getPet(PET_ID);
        verify(userRepository, times(1)).getUserByPetId(PET_ID);
        verify(healthService, times(1)).getHealthInfoForBreed(BREED_ID, petAge);
        verify(weatherService, times(1)).getCurrentConditions(user);
        verify(notificationRepository).saveNotification(
                argThat(actualResult ->
                                actualResult.getResultId().equals(healthAnalysisResult.getResultId()) &&
                                        actualResult.getPet().equals(healthAnalysisResult.getPet()) &&
                                        actualResult.getMessage().equals(healthAnalysisResult.getMessage())
                        // Optionally add other fields you care about
                ),
                eq(user)
        );    }

    @Test
    void analyzeHealthRecord_shouldThrowInvalidPetExceptionWhenPetNotFound() {
        // Given
        when(petRepository.getPet(PET_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidPetException.class, () -> petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord));
        verify(petRepository, times(1)).getPet(PET_ID);
    }

    @Test
    void analyzeHealthRecord_shouldThrowInvalidUserExceptionWhenUserNotFound() {
        // Given
        when(petRepository.getPet(PET_ID)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(PET_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidUserException.class, () -> petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord));
        verify(petRepository, times(1)).getPet(PET_ID);
        verify(userRepository, times(1)).getUserByPetId(PET_ID);
    }

    @Test
    void adjustForWeather_shouldAdjustFoodIntakeForHotWeather() {
        // Given
        double normFoodIntake = 50.0;
        double expectedAdjustedFoodIntake = 45.0; // Decreased by 10% for food in hot weather

        // When
        double adjustedFoodIntake = invokePrivateMethod("adjustForWeather", normFoodIntake, weatherConditions, "food");

        // Then
        assertEquals(expectedAdjustedFoodIntake, adjustedFoodIntake);
    }

    @Test
    void adjustForWeather_shouldAdjustWaterIntakeForHotWeather() {
        // Given
        double normWaterIntake = 60.0;
        double expectedAdjustedWaterIntake = 72.0; // Increased by 20% for water in hot weather

        // Create a mock or real WeatherConditions object where isHot() returns true
        WeatherConditions weatherConditions2 = mock(WeatherConditions.class);
        when(weatherConditions2.isHot()).thenReturn(true);

        // When
        double adjustedWaterIntake = invokePrivateMethod("adjustForWeather", normWaterIntake, weatherConditions2, "water");

        // Then
        assertEquals(expectedAdjustedWaterIntake, adjustedWaterIntake);
    }


    @Test
    void adjustForWeather_shouldNotAdjustIntakeForNonHotWeather() {
        // Given
        WeatherConditions normalWeather = WeatherConditions.builder().temperature(30).build();
        double normFoodIntake = 50.0;
        double normWaterIntake = 60.0;

        // When
        double adjustedFoodIntake = invokePrivateMethod("adjustForWeather", normFoodIntake, normalWeather, "food");
        double adjustedWaterIntake = invokePrivateMethod("adjustForWeather", normWaterIntake, normalWeather, "water");

        // Then
        assertEquals(normFoodIntake, adjustedFoodIntake);
        assertEquals(normWaterIntake, adjustedWaterIntake);
    }

    @Test
    void adjustForWeather_shouldNotAdjustForColdWeather() {
        // Given
        WeatherConditions coldWeather = WeatherConditions.builder().temperature(15).build();
        double normFoodIntake = 50.0;
        double expectedFoodIntake = 50.0;

        // When
        double adjustedFood = invokePrivateMethod("adjustForWeather", normFoodIntake, coldWeather, "food");

        // Then
        assertEquals(expectedFoodIntake, adjustedFood);
    }

    @Test
    void detectAnomalies_shouldDetectAnomaliesInHealthRecord() {
        // Given
        double adjustedFoodIntake = 45.0;
        double adjustedWaterIntake = 48.0;

        // When
        List<String> anomalies = invokePrivateMethod("detectAnomalies", healthRecord, adjustedFoodIntake, adjustedWaterIntake, breed);

        // Then
        assertTrue(anomalies.contains("Low food intake."));
        assertTrue(anomalies.contains("Low water intake."));
        assertTrue(anomalies.contains("Mood deviation detected."));
        assertTrue(anomalies.contains("Low activity level."));
    }

    @Test
    void detectAnomalies_shouldDetectLowFoodIntakeOnly() {
        // Given
        HealthRecord healthRecord2 = HealthRecord.builder().foodIntake(30.0).waterIntake(60.0).activityLevel(5).mood(Mood.builder().name("Happy").build()).build();
        double adjustedFood = 50.0;
        double adjustedWater = 60.0;

        // When
        List<String> anomalies = invokePrivateMethod("detectAnomalies", healthRecord2, adjustedFood, adjustedWater, breed);

        // Then
        assertTrue(anomalies.contains("Low food intake."));
        assertFalse(anomalies.contains("Low water intake."));
    }

    @Test
    void mapActivityLevelToHours_shouldMapCorrectActivityLevel() {
        // When & Then
        assertEquals(0.25, invokePrivateMethod("mapActivityLevelToHours", 1));
        assertEquals(0.5, invokePrivateMethod("mapActivityLevelToHours", 2));
        assertEquals(0.75, invokePrivateMethod("mapActivityLevelToHours", 3));
        assertEquals(1.0, invokePrivateMethod("mapActivityLevelToHours", 4));
        assertEquals(1.5, invokePrivateMethod("mapActivityLevelToHours", 5));
        assertEquals(2.0, invokePrivateMethod("mapActivityLevelToHours", 6));
        assertEquals(2.5, invokePrivateMethod("mapActivityLevelToHours", 7));
        assertEquals(3.0, invokePrivateMethod("mapActivityLevelToHours", 8));
        assertEquals(4.0, invokePrivateMethod("mapActivityLevelToHours", 9));
        assertEquals(5.0, invokePrivateMethod("mapActivityLevelToHours", 10));
    }

    @Test
    void analyzeHistoricalTrends_shouldDetectHistoricalAnomalies() {
        // Given
        List<HealthRecord> records = Arrays.asList(
                HealthRecord.builder().foodIntake(20.0).waterIntake(20.0).mood(Mood.builder().name("Sad").build()).build(),
                HealthRecord.builder().foodIntake(20.0).waterIntake(20.0).mood(Mood.builder().name("Sad").build()).build(),
                HealthRecord.builder().foodIntake(20.0).waterIntake(20.0).mood(Mood.builder().name("Sad").build()).build()
        );

        // When
        List<String> anomalies = invokePrivateMethod("analyzeHistoricalTrends", records);

        // Then
        assertTrue(anomalies.contains("Consistent low food intake over several days."));
        assertTrue(anomalies.contains("Consistent low water intake over several days."));
        assertTrue(anomalies.contains("Your pet seems sad for several days. Consider spending more time playing or engaging with them."));
    }

    // Helper method to invoke private methods using reflection
    private <T> T invokePrivateMethod(String methodName, Object... params) {
        try {
            Method method = PetHealthAnalyzerImpl.class.getDeclaredMethod(methodName, getParameterTypes(params));
            method.setAccessible(true);
            return (T) method.invoke(petHealthAnalyzer, params);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?>[] getParameterTypes(Object[] params) {
        Class<?>[] parameterTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Double) {
                parameterTypes[i] = double.class; // Handle primitive double
            } else if (params[i] instanceof Integer) {
                parameterTypes[i] = int.class; // Handle primitive double
            } else if (params[i] instanceof List) {
                parameterTypes[i] = List.class; // Use the interface type for lists
            } else {
                parameterTypes[i] = params[i].getClass();
            }
        }
        return parameterTypes;
    }
}
