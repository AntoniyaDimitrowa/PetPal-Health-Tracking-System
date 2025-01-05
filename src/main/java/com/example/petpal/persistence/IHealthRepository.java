package com.example.petpal.persistence;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;

import java.util.List;
import java.util.Optional;

public interface IHealthRepository {
    Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age);
    List<BreedHealthInfo> getHealthInfoByBreedId(long breedId);

    Long createHealthInfoForBreed(BreedHealthInfo info);

    List<HealthRecord> getHealthRecordsByPetId(long petId);
    List<HealthRecord> getHealthRecentRecordsByPetId(long petId, int numberOfRecords);

    Long createHealthRecordToPet(long petId, HealthRecord healthRecord);

    List<Object[]> findHealthRecordsWithNormsForPet(Long petId, int month, int year);

    List<Object[]> findMoodDistributionForPet(Long petId, int month, int year);
}
