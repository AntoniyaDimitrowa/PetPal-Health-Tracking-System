package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.controller.dto.health.PetStatisticsDTO;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.IPetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;
    private final IHealthRepository healthRepository;
    private final IBreedRepository breedRepository;
    private final IMoodRepository moodRepository;

    @Override
    public List<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        return healthRepository.getHealthRecordsByPetId(petId);
    }

    @Override
    public Long createHealthRecord(Long petId, HealthRecord healthRecord, Long moodId) throws InvalidPetException, InvalidMoodException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        Mood mood = moodRepository.getMoodById(moodId)
                .orElseThrow(() -> new InvalidMoodException(moodId));

        healthRecord.setMood(mood);
        return healthRepository.createHealthRecordToPet(petId, healthRecord);
    }

    @Override
    public PetStatisticsDTO getStatisticsForPet(Long petId, int month, int year) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
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
                .collect(Collectors.toList());

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
    public Long createHealthInfoForBreed(Long breedId, BreedHealthInfo info) throws InvalidBreedException {
        Optional<Breed> breed = breedRepository.getBreedById(breedId);
        if(breed.isEmpty()) {
            throw new InvalidBreedException(breedId);
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
