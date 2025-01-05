package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.IPetHealthAnalyzer;
import com.example.petpal.business.IWeatherService;
import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.persistence.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PetHealthAnalyzerImpl implements IPetHealthAnalyzer {
    private final IHealthService healthService;
    private final IWeatherService weatherService;
    private final IPetRepository petRepository;
    private final IUserRepository userRepository;
    private final NotificationService notificationService;
    private final INotificationRepository notificationRepository;

    @Override
    public HealthAnalysisResult analyzeHealthRecord(Long petId, HealthRecord newRecord) throws InvalidPetException, InvalidUserException {
        // Fetch pet and breed details
        Pet pet = petRepository.getPet(petId)
                .orElseThrow(() -> new InvalidPetException(petId));

        User user = userRepository.getUserByPetId(pet.getId())
                .orElseThrow(() -> new InvalidUserException("User not found for the pet"));

        Breed breed = pet.getBreed();

        // Fetch health information
        BreedHealthInfo healthInfo = healthService.getHealthInfoForBreed(breed.getId(), calculatePetAge(pet));

        // Fetch weather conditions
        WeatherConditions currentWeather = weatherService.getCurrentConditions(user);

        // Adjust norms based on weather
        double adjustedFoodIntake = adjustForWeather(healthInfo.getNormalFoodIntake(), currentWeather, "food");
        double adjustedWaterIntake = adjustForWeather(healthInfo.getNormalWaterIntake(), currentWeather, "water");

        List<String> anomalies = detectAnomalies(newRecord, adjustedFoodIntake, adjustedWaterIntake, breed);

        // Add historical trends to anomalies
        List<HealthRecord> recentRecords = healthService.getRecentRecords(petId, 5);
        anomalies.addAll(analyzeHistoricalTrends(recentRecords));

        // Generate notification message
        String notificationMessage = notificationService.generateNotification(String.join(", ", anomalies));

        HealthAnalysisResult result = new HealthAnalysisResult(newRecord.getId(), new Date(), pet, notificationMessage, false);

        notificationRepository.saveNotification(result, user);
        // Build and return the analysis result
        return result;
    }

    private int calculatePetAge(Pet pet) {
        Date birthdate = pet.getBirthdate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthdate);
        int birthYear = calendar.get(Calendar.YEAR);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return currentYear - birthYear;
    }

    private double adjustForWeather(double normValue, WeatherConditions weather, String type) {
        if (weather.isHot()) {
            if ("water".equals(type)) {
                return normValue * 1.2; // Increase by 20% in hot weather
            } else if ("food".equals(type)) {
                return normValue * 0.9; // Decrease by 10% in hot weather
            }
        }
        return normValue;
    }

    private List<String> detectAnomalies(HealthRecord healthRecord, double adjustedFood, double adjustedWater, Breed breed) {
        List<String> anomalies = new ArrayList<>();
        if (healthRecord.getFoodIntake() < adjustedFood * 0.8) anomalies.add("Low food intake.");
        if (healthRecord.getWaterIntake() < adjustedWater * 0.8) anomalies.add("Low water intake.");
        if (healthRecord.getMood().getName().equals("Sad") || healthRecord.getMood().getName().equals("Angry")) {
            anomalies.add("Mood deviation detected.");
        }
        if(mapActivityLevelToHours(healthRecord.getActivityLevel()) < breed.getMinimumExercisePerDay()) {
            anomalies.add("Low activity level.");
        }
        return anomalies;
    }

    private double mapActivityLevelToHours(int activityLevel) {
        switch (activityLevel) {
            case 1: return 0.25;  // 15 minutes
            case 2: return 0.5;   // 30 minutes
            case 3: return 0.75;  // 45 minutes
            case 4: return 1.0;   // 1 hour
            case 5: return 1.5;   // 1.5 hours
            case 6: return 2.0;   // 2 hours
            case 7: return 2.5;   // 2.5 hours
            case 8: return 3.0;   // 3 hours
            case 9: return 4.0;   // 4 hours
            case 10: return 5.0;  // 5+ hours
            default: throw new IllegalArgumentException("Invalid activity level: " + activityLevel);
        }
    }
    private List<String> analyzeHistoricalTrends(List<HealthRecord> records) {
        List<String> anomalies = new ArrayList<>();
        int lowFoodCount = 0;
        int lowWaterCount = 0;
        int sleepyDaysCount = 0;
        int sadDaysCount = 0;
        int angryDaysCount = 0;
        int neutralDaysCount = 0;

        for (HealthRecord healthRecord : records) {
            if (healthRecord.getFoodIntake() < 50.0) {
                lowFoodCount++;
            }
            if (healthRecord.getWaterIntake() < 50.0) {
                lowWaterCount++;
            }

            // Mood checks
            switch (healthRecord.getMood().getName()) {
                case "Sleepy":
                    if (healthRecord.getActivityLevel() < 4) { // Assuming activity level is on a scale from 1-5
                        sleepyDaysCount++;
                    }
                    break;
                case "Sad":
                    sadDaysCount++;
                    break;
                case "Angry":
                    angryDaysCount++;
                    break;
                case "Neutral":
                    neutralDaysCount++;
                    break;
                default:
                    break; // Happy or Excited do not count as issues
            }
        }

        // Generate anomalies
        if (lowFoodCount >= 3) {
            anomalies.add("Consistent low food intake over several days.");
        }
        if (lowWaterCount >= 3) {
            anomalies.add("Consistent low water intake over several days.");
        }
        if (sleepyDaysCount >= 3) {
            anomalies.add("Your pet has been sleepy and inactive for several days.");
        }
        if (sadDaysCount >= 3) {
            anomalies.add("Your pet seems sad for several days. Consider spending more time playing or engaging with them.");
        }
        if (angryDaysCount >= 2) {
            anomalies.add("Your pet has been angry for a few days. Please consult a vet if this continues.");
        }
        if (neutralDaysCount >= 3) {
            anomalies.add("Your pet's mood has been neutral for several days. Consider increasing engagement to improve their happiness.");
        }

        return anomalies;
    }

}
