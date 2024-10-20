package com.example.petpal.persistence;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.HealthRecordEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface IHealthRepository {
    Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age);
    ArrayList<BreedHealthInfo> getHealthInfoByBreedId(long breedId);

    Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info);

    ArrayList<HealthRecord> getHealthRecordsByPetId(long petId);

    Long createHealthRecordToPet(long petId, HealthRecord healthRecord);

}
