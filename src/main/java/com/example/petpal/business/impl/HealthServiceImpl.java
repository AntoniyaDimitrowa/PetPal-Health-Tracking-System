package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;
    private final IHealthRepository healthRepository;
    private final IBreedRepository breedRepository;

    @Override
    public List<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        return healthRepository.getHealthRecordsByPetId(petId);
    }

    @Override
    public Long createHealthRecord(Long petId, HealthRecord healthRecord) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        return healthRepository.createHealthRecordToPet(petId, healthRecord);
    }

    @Override
    public BreedHealthInfo getHealthInfoForBreed(Long breedId, int age) {
        return healthRepository.getHealthInfoForBreed(breedId, age)
                .orElse(null);
    }

    @Override
    public Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info) throws InvalidBreedException {
        if(breedRepository.getBreedById(breedId).isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        return healthRepository.createHealthInfoForBreed(breedId, ageRangeStart, ageRangeEnd, info);
    }

    @Override
    public List<BreedHealthInfo> getHealthInfoByBreedId(Long breedId) {
        return healthRepository.getHealthInfoByBreedId(breedId);
    }
}
