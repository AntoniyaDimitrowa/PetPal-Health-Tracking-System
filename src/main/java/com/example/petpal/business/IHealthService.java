package com.example.petpal.business;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.List;

public interface IHealthService {
    List<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException;

    Long createHealthRecord(Long petId, HealthRecord healthRecord, Long moodId) throws InvalidPetException, InvalidMoodException;

    BreedHealthInfo getHealthInfoForBreed(Long breedId, int age);

    Long createHealthInfoForBreed(Long breedId, BreedHealthInfo info) throws InvalidBreedException;

    List<BreedHealthInfo> getHealthInfoByBreedId(Long breedId);

}
