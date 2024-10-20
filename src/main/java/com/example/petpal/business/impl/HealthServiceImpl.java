package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.persistence.converters.HealthConverter;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.PetEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;
    private final IHealthRepository healthRepository;

    @Override
    public ArrayList<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException {
        PetEntity pet = petRepository.getPet(petId)
                .orElseThrow(() -> new InvalidPetException(petId));

        return healthRepository.getHealthRecordsByPetId(petId);
    }

    @Override
    public Long createHealthRecord(Long petId, HealthRecord healthRecord) throws InvalidPetException {
        PetEntity pet = petRepository.getPet(petId)
                .orElseThrow(() -> new InvalidPetException(petId));

        return healthRepository.createHealthRecordToPet(petId, healthRecord);
    }

    @Override
    public BreedHealthInfo getHealthInfoForBreed(Long breedId, int age) {
        return healthRepository.getHealthInfoForBreed(breedId, age)
                .orElse(null);
    }

    @Override
    public Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info) {
        return healthRepository.createHealthInfoForBreed(breedId, ageRangeStart, ageRangeEnd, info);
    }

    @Override
    public ArrayList<BreedHealthInfo> getHealthInfoByBreedId(Long breedId) {
        return healthRepository.getHealthInfoByBreedId(breedId);
    }
}
