package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.IBreedHealthInfoRepositoryJPA;
import com.example.petpal.persistence.IHealthRecordRepositoryJPA;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.persistence.IPetRepositoryJPA;
import com.example.petpal.persistence.converters.HealthConverter;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;
import com.example.petpal.persistence.entity.PetEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HealthRepositoryImpl implements IHealthRepository {


    private final IBreedHealthInfoRepositoryJPA breedHealthInfoRepository;

    private final IHealthRecordRepositoryJPA healthRecordRepository;
    private final IPetRepositoryJPA petRepositoryJPA;

    @Override
    public Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age) {
        Optional<BreedHealthInfoEntity> healthInfoEntity = breedHealthInfoRepository.findByBreedIdAndAgeRangeStartLessThanEqualAndAgeRangeEndGreaterThanEqual(breedId, age);

        return healthInfoEntity.map(HealthConverter::convertFromBreedHealthInfoEntityToBreedHealthInfo);
    }


    @Override
    public List<BreedHealthInfo> getHealthInfoByBreedId(long breedId) {
        List<BreedHealthInfoEntity> healthInfoEntities = breedHealthInfoRepository.findAllByBreedId(breedId);

        return HealthConverter.convertFromEntitiesToBreedHealthInfos(new ArrayList<>(healthInfoEntities));
    }

    @Override
    public Long createHealthInfoForBreed(BreedHealthInfo info) {
        BreedHealthInfoEntity entity = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoEntity(info);

        BreedHealthInfoEntity savedEntity = breedHealthInfoRepository.save(entity);
        return savedEntity.getId();
    }


    @Override
    public List<HealthRecord> getHealthRecordsByPetId(long petId) {
        List<HealthRecordEntity> healthRecordEntities = healthRecordRepository.findByPetId(petId);

        return HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(healthRecordEntities);
    }

    @Override
    public List<HealthRecord> getHealthRecentRecordsByPetId(long petId, int numberOfRecords) {
        List<HealthRecordEntity> healthRecordEntities = healthRecordRepository.findRecentRecordsByPetId(petId, numberOfRecords);

        return HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(healthRecordEntities);
    }

    @Override
    public Long createHealthRecordToPet(long petId, HealthRecord healthRecord) {
        PetEntity pet = petRepositoryJPA.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        HealthRecordEntity entity = HealthConverter.convertFromHealthRecordToHealthRecordEntity(healthRecord);
        entity.setPet(pet);

        HealthRecordEntity savedEntity = healthRecordRepository.save(entity);
        return savedEntity.getId();
    }

    @Override
    public List<Object[]> findHealthRecordsWithNormsForPet(Long petId, int month, int year) {
        return healthRecordRepository.findHealthRecordsWithNormsForPet(petId, month, year);
    }

    @Override
    public List<Object[]> findMoodDistributionForPet(Long petId, int month, int year) {
        return healthRecordRepository.findMoodDistributionForPet(petId, month, year);
    }
}
