package com.example.petpal.business;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.controller.dto.health.PetStatisticsDTO;

import java.util.List;

public interface IHealthService {
    List<HealthRecord> getHealthRecordsByPetId(Long petId) throws InvalidPetException;

    Long createHealthRecord(Long petId, HealthRecord healthRecord, Long moodId) throws InvalidPetException, InvalidMoodException;

    PetStatisticsDTO getStatisticsForPet(Long petId, int month, int year) throws InvalidPetException;

    BreedHealthInfo getHealthInfoForBreed(Long breedId, int age);

    Long createHealthInfoForBreed(Long breedId, BreedHealthInfo info) throws InvalidBreedException;

    List<BreedHealthInfo> getHealthInfoByBreedId(Long breedId);

    List<HealthRecord> getRecentRecords(Long petId, int numberOfRecords) throws InvalidPetException;
}
