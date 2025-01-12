package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.controller.dto.health.PetStatisticsDTO;
import com.example.petpal.persistence.*;
import com.example.petpal.business.exception.InvalidPetException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;
    private final IHealthRepository healthRepository;
    private final IBreedRepository breedRepository;
    private final IMoodRepository moodRepository;
    private final IUserRepository userRepository;
    private final IAccessToken requestAccessToken;

    @Override
    public List<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException, UnauthorizedDataAccessException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }
        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }
        return healthRepository.getHealthRecordsByPetId(petId);
    }

    @Override
    public Long createHealthRecord(Long petId, HealthRecord healthRecord, Long moodId) throws InvalidPetException, InvalidMoodException, UnauthorizedDataAccessException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }

        Mood mood = moodRepository.getMoodById(moodId)
                .orElseThrow(() -> new InvalidMoodException(moodId));

        healthRecord.setMood(mood);
        return healthRepository.createHealthRecordToPet(petId, healthRecord);
    }

    @Override
    public PetStatisticsDTO getStatisticsForPet(Long petId, int month, int year) throws InvalidPetException, UnauthorizedDataAccessException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }
        // Fetch health records with norms
        List<Object[]> healthRecords = healthRepository.findHealthRecordsWithNormsForPet(petId, month, year);

        // Fetch mood distribution
        List<Object[]> moodDistribution = healthRepository.findMoodDistributionForPet(petId, month, year);

        // Map health records to DTO
        List<PetStatisticsDTO.FoodWaterIntakeDTO> foodIntake = new ArrayList<>();
        List<PetStatisticsDTO.FoodWaterIntakeDTO> waterIntake = new ArrayList<>();
        List<PetStatisticsDTO.ActivityLevelDTO> activityLevel = new ArrayList<>();

        for (Object[] healthRecord : healthRecords) {
            Date date = (Date) healthRecord[0];
            Double foodIntakeValue = healthRecord[1] instanceof Double food ? food : 0.0;
            Double waterIntakeValue = healthRecord[2] instanceof Double water ? water : 0.0;
            Integer activityLevelValue = healthRecord[3] instanceof Integer activity ? activity : 0;
            Double normalFoodIntake = healthRecord[4] instanceof Double normalFood ? normalFood : 0.0;
            Double normalWaterIntake = healthRecord[5] instanceof Double normalWater ? normalWater : 0.0;
            Double minimumExercise = healthRecord[6] instanceof Double exercise ? exercise : 0.0;


            foodIntake.add(new PetStatisticsDTO.FoodWaterIntakeDTO(date, foodIntakeValue, normalFoodIntake));
            waterIntake.add(new PetStatisticsDTO.FoodWaterIntakeDTO(date, waterIntakeValue, normalWaterIntake));
            activityLevel.add(new PetStatisticsDTO.ActivityLevelDTO(date, (long) activityLevelValue, minimumExercise));
        }

        // Map mood distribution to DTO
        List<PetStatisticsDTO.MoodDistributionDTO> moodData = moodDistribution.stream()
                .map(moodRecord -> new PetStatisticsDTO.MoodDistributionDTO((String) moodRecord[1], (long) moodRecord[2]))
                .toList();

        // Construct DTO
        return PetStatisticsDTO.builder()
                .foodIntake(foodIntake)
                .waterIntake(waterIntake)
                .activityLevel(activityLevel)
                .moodDistribution(moodData)
                .build();
    }

    @Override
    public BreedHealthInfo getHealthInfoForBreed(Long breedId, int age) {
        return healthRepository.getHealthInfoForBreed(breedId, age)
                .orElse(null);
    }

    @Override
    public Long createHealthInfoForBreed(Long breedId, Long userId, BreedHealthInfo info) throws InvalidBreedException, UnauthorizedDataAccessException {
        Optional<Breed> breed = breedRepository.getBreedById(breedId);
        if(breed.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        Optional<User> user = userRepository.getUserById(userId);
        if(user.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), userId)) {
            throw new UnauthorizedDataAccessException();
        }

        info.setBreed(breed.get());
        return healthRepository.createHealthInfoForBreed(info);
    }

    @Override
    public List<BreedHealthInfo> getHealthInfoByBreedId(Long breedId) {
        return healthRepository.getHealthInfoByBreedId(breedId);
    }

    @Override
    public List<HealthRecord> getRecentRecords(Long petId, int numberOfRecords) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        return healthRepository.getHealthRecentRecordsByPetId(petId, numberOfRecords);
    }
}
