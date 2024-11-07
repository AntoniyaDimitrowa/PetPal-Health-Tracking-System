package com.example.petpal.persistence;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;

import java.util.List;
import java.util.Optional;

public interface IHealthRepository {
    Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age);
    List<BreedHealthInfo> getHealthInfoByBreedId(long breedId);

    Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info);

    List<HealthRecord> getHealthRecordsByPetId(long petId);

    Long createHealthRecordToPet(long petId, HealthRecord healthRecord);

}
