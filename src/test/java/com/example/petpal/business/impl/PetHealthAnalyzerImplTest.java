//package com.example.petpal.business.impl;
//
//import com.example.petpal.business.IHealthService;
//import com.example.petpal.business.IWeatherService;
//import com.example.petpal.persistence.*;
//import com.example.petpal.business.domain.*;
//import com.example.petpal.business.exception.InvalidPetException;
//import com.example.petpal.business.exception.InvalidUserException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.lang.reflect.Method;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@Tag("unit")
//class PetHealthAnalyzerImplTest {
//
//    @Mock
//    private IHealthService healthService;
//
//    @Mock
//    private IWeatherService weatherService;
//
//    @Mock
//    private IPetRepository petRepository;
//
//    @Mock
//    private IUserRepository userRepository;
//
//    @Mock
//    private NotificationGenerator notificationGenerator;
//
//    @Mock
//    private INotificationRepository notificationRepository;
//
//    @InjectMocks
//    private PetHealthAnalyzerImpl petHealthAnalyzer;
//
//    private static final Long PET_ID = 1L;
//    private static final Long USER_ID = 1L;
//    private static final Long BREED_ID = 1L;
//    private static final Long HEALTH_RECORD_ID = 1L;
//
//    private static final Breed breed = Breed.builder()
//            .id(BREED_ID)
//            .minimumExercisePerDay(1)
//            .build();
//
//    private static final Pet pet = Pet.builder()
//            .id(PET_ID)
//            .breed(breed)
//            .birthdate(new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime())
//            .build();
//
//    private static final User user = User.builder()
//            .id(USER_ID)
//            .name("John Doe")
//            .pets(Optional.of(List.of(pet)))
//            .build();
//
//    private static final HealthRecord healthRecord = HealthRecord.builder()
//            .id(HEALTH_RECORD_ID)
//            .foodIntake(40.0)
//            .waterIntake(30.0)
//            .activityLevel(3)
//            .mood(Mood.builder().name("Sad").build())
//            .build();
//
//    private static final BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder()
//            .normalFoodIntake(50.0)
//            .normalWaterIntake(60.0)
//            .build();
//
//    private static final WeatherConditions weatherConditions = WeatherConditions.builder()
//            .temperature(32)
//            .build();
//
//    private static final HealthAnalysisResult healthAnalysisResult = HealthAnalysisResult.builder()
//            .resultId(HEALTH_RECORD_ID)
//            .date(new Date())
//            .pet(pet)
//            .message("Low food intake. Low water intake. Mood deviation detected. Low activity level.")
//            .build();
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void analyzeHealthRecord_shouldAnalyzeHealthRecordAndGenerateNotification() throws InvalidPetException, InvalidUserException {
//        // Given
//        when(petRepository.getPet(PET_ID)).thenReturn(Optional.of(pet));
//        when(userRepository.getUserByPetId(PET_ID)).thenReturn(Optional.of(user));
//        when(healthService.getHealthInfoForBreed(BREED_ID, 9)).thenReturn(breedHealthInfo);
//        when(weatherService.getCurrentConditions(user)).thenReturn(weatherConditions);
//        when(healthService.getRecentRecords(PET_ID, 5)).thenReturn(Collections.emptyList());
//        when(notificationGenerator.generateNotification(anyString())).thenReturn("Notification message");
//
//        // When
//        HealthAnalysisResult result = petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("Low food intake. Low water intake. Mood deviation detected. Low activity level.", result.getMessage());
//        verify(petRepository, times(1)).getPet(PET_ID);
//        verify(userRepository, times(1)).getUserByPetId(PET_ID);
//        verify(healthService, times(1)).getHealthInfoForBreed(BREED_ID, 9);
//        verify(weatherService, times(1)).getCurrentConditions(user);
//        verify(notificationRepository, times(1)).saveNotification(healthAnalysisResult, user);
//    }
//
//    @Test
//    void analyzeHealthRecord_shouldThrowInvalidPetExceptionWhenPetNotFound() {
//        // Given
//        when(petRepository.getPet(PET_ID)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(InvalidPetException.class, () -> petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord));
//        verify(petRepository, times(1)).getPet(PET_ID);
//    }
//
//    @Test
//    void analyzeHealthRecord_shouldThrowInvalidUserExceptionWhenUserNotFound() {
//        // Given
//        when(petRepository.getPet(PET_ID)).thenReturn(Optional.of(pet));
//        when(userRepository.getUserByPetId(PET_ID)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(InvalidUserException.class, () -> petHealthAnalyzer.analyzeHealthRecord(PET_ID, healthRecord));
//        verify(petRepository, times(1)).getPet(PET_ID);
//        verify(userRepository, times(1)).getUserByPetId(PET_ID);
//    }
//
//    @Test
//    void adjustForWeather_shouldAdjustFoodIntakeForHotWeather() {
//        // Given
//        double normFoodIntake = 50.0;
//        double expectedAdjustedFoodIntake = 45.0; // Decreased by 10% for food in hot weather
//
//        // When
//        double adjustedFoodIntake = invokePrivateMethod("adjustForWeather", normFoodIntake, weatherConditions, "food");
//
//        // Then
//        assertEquals(expectedAdjustedFoodIntake, adjustedFoodIntake);
//    }
//
//    @Test
//    void adjustForWeather_shouldAdjustWaterIntakeForHotWeather() {
//        // Given
//        double normWaterIntake = 60.0;
//        double expectedAdjustedWaterIntake = 72.0; // Increased by 20% for water in hot weather
//
//        // Create a mock or real WeatherConditions object where isHot() returns true
//        WeatherConditions weatherConditions2 = mock(WeatherConditions.class);
//        when(weatherConditions2.isHot()).thenReturn(true);
//
//        // When
//        double adjustedWaterIntake = invokePrivateMethod("adjustForWeather", normWaterIntake, weatherConditions2, "water");
//
//        // Then
//        assertEquals(expectedAdjustedWaterIntake, adjustedWaterIntake);
//    }
//
//
//    @Test
//    void adjustForWeather_shouldNotAdjustIntakeForNonHotWeather() {
//        // Given
//        WeatherConditions normalWeather = WeatherConditions.builder().temperature(30).build();
//        double normFoodIntake = 50.0;
//        double normWaterIntake = 60.0;
//
//        // When
//        double adjustedFoodIntake = invokePrivateMethod("adjustForWeather", normFoodIntake, normalWeather, "food");
//        double adjustedWaterIntake = invokePrivateMethod("adjustForWeather", normWaterIntake, normalWeather, "water");
//
//        // Then
//        assertEquals(normFoodIntake, adjustedFoodIntake);
//        assertEquals(normWaterIntake, adjustedWaterIntake);
//    }
//
//    @Test
//    void detectAnomalies_shouldDetectAnomaliesInHealthRecord() {
//        // Given
//        double adjustedFoodIntake = 45.0;
//        double adjustedWaterIntake = 48.0;
//
//        // When
//        List<String> anomalies = invokePrivateMethod("detectAnomalies", healthRecord, adjustedFoodIntake, adjustedWaterIntake, breed);
//
//        // Then
//        assertTrue(anomalies.contains("Low food intake."));
//        assertTrue(anomalies.contains("Low water intake."));
//        assertTrue(anomalies.contains("Mood deviation detected."));
//        assertTrue(anomalies.contains("Low activity level."));
//    }
//
//    @Test
//    void mapActivityLevelToHours_shouldMapCorrectActivityLevel() {
//        // When & Then
//        assertEquals(0.5, invokePrivateMethod("mapActivityLevelToHours", 2));
//        assertEquals(1.0, invokePrivateMethod("mapActivityLevelToHours", 4));
//        assertEquals(2.0, invokePrivateMethod("mapActivityLevelToHours", 6));
//    }
//
//    @Test
//    void analyzeHistoricalTrends_shouldDetectHistoricalAnomalies() {
//        // Given
//        List<HealthRecord> records = Arrays.asList(
//                HealthRecord.builder().foodIntake(40.0).waterIntake(40.0).mood(Mood.builder().name("Sad").build()).build(),
//                HealthRecord.builder().foodIntake(30.0).waterIntake(40.0).mood(Mood.builder().name("Sad").build()).build(),
//                HealthRecord.builder().foodIntake(60.0).waterIntake(50.0).mood(Mood.builder().name("Neutral").build()).build()
//        );
//
//        // When
//        List<String> anomalies = invokePrivateMethod("analyzeHistoricalTrends", records);
//
//        // Then
//        assertTrue(anomalies.contains("Consistent low food intake over several days."));
//        assertTrue(anomalies.contains("Consistent low water intake over several days."));
//        assertTrue(anomalies.contains("Your pet seems sad for several days. Consider spending more time playing or engaging with them."));
//    }
//
//    // Helper method to invoke private methods using reflection
//    private <T> T invokePrivateMethod(String methodName, Object... params) {
//        try {
//            Method method = PetHealthAnalyzerImpl.class.getDeclaredMethod(methodName, getParameterTypes(params));
//            method.setAccessible(true);
//            return (T) method.invoke(petHealthAnalyzer, params);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Class<?>[] getParameterTypes(Object[] params) {
//        Class<?>[] parameterTypes = new Class[params.length];
//        for (int i = 0; i < params.length; i++) {
//            parameterTypes[i] = params[i].getClass();
//        }
//        return parameterTypes;
//    }
//}
