package com.example.petpal.business;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.ArrayList;

public interface IHealthService {
    ArrayList<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException;

    Long createHealthRecord(Long petId, HealthRecord healthRecord) throws InvalidPetException;

    BreedHealthInfo getHealthInfoForBreed(Long breedId, int age);

    Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info);

    ArrayList<BreedHealthInfo> getHealthInfoByBreedId(Long breedId);

}
