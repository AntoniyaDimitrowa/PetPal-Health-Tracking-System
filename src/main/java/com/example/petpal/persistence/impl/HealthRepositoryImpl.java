package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.IBreedHealthInfoRepositoryJPA;
import com.example.petpal.persistence.IHealthRecordRepositoryJPA;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.persistence.converters.HealthConverter;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HealthRepositoryImpl implements IHealthRepository {

    @Autowired
    private IBreedHealthInfoRepositoryJPA breedHealthInfoRepository;

    @Autowired
    private IHealthRecordRepositoryJPA healthRecordRepository;

    @Override
    public Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age) {
        //TODO fix this method
        Optional<BreedHealthInfoEntity> healthInfoEntity = breedHealthInfoRepository.findByBreedIdAndAgeRangeStartLessThanEqualAndAgeRangeEndGreaterThanEqual(breedId, age, 1);

        return healthInfoEntity.map(HealthConverter::convertFromBreedHealthInfoEntityToBreedHealthInfo);
    }


    @Override
    public ArrayList<BreedHealthInfo> getHealthInfoByBreedId(long breedId) {
        List<BreedHealthInfoEntity> healthInfoEntities = breedHealthInfoRepository.findAllByBreedId(breedId);

        return HealthConverter.convertFromEntitiesToBreedHealthInfos(new ArrayList<>(healthInfoEntities));
    }

    @Override
    public Long createHealthInfoForBreed(Long breedId, int ageRangeStart, int ageRangeEnd, BreedHealthInfo info) {
        BreedHealthInfoEntity entity = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoEntity(info);

        entity.getBreed().setId(breedId);
        entity.setAgeRangeStart(ageRangeStart);
        entity.setAgeRangeEnd(ageRangeEnd);

        BreedHealthInfoEntity savedEntity = breedHealthInfoRepository.save(entity);
        return savedEntity.getId();
    }


    @Override
    public ArrayList<HealthRecord> getHealthRecordsByPetId(long petId) {
        List<HealthRecordEntity> healthRecordEntities = healthRecordRepository.findByPetId(petId);

        return HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(healthRecordEntities);
    }

    @Override
    public Long createHealthRecordToPet(long petId, HealthRecord healthRecord) {
        HealthRecordEntity entity = HealthConverter.convertFromHealthRecordToHealthRecordEntity(healthRecord);

        HealthRecordEntity savedEntity = healthRecordRepository.save(entity);
        return savedEntity.getId();
    }
}
