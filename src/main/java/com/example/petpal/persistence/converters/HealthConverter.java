package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class HealthConverter {

    public static List<HealthRecord> convertFromHealthRecordEntitiesToHealthRecords(List<HealthRecordEntity> entities) {
        List<HealthRecord> result = new ArrayList<>();
        for (HealthRecordEntity entity : entities) {
            result.add(convertFromHealthRecordEntityToHealthRecord(entity));
        }
        return result;
    }

    public static HealthRecord convertFromHealthRecordEntityToHealthRecord(HealthRecordEntity entity) {
        if (entity == null) return null;
        return HealthRecord.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .foodIntake(entity.getFoodIntake())
                .waterIntake(entity.getWaterIntake())
                .mood(entity.getMood() != null ? MoodConverter.convertFromMoodEntityToMood(entity.getMood()) : null)
                .activityLevel(entity.getActivityLevel())
                .socialInteraction(entity.getSocialInteraction())
                .notes(entity.getNotes())
                .build();
    }

    public static List<HealthRecordEntity> convertFromHealthRecordsToHealthRecordEntities(List<HealthRecord> records) {
        List<HealthRecordEntity> result = new ArrayList<>();
        for (HealthRecord record : records) {
            result.add(convertFromHealthRecordToHealthRecordEntity(record));
        }
        return result;
    }

    public static HealthRecordEntity convertFromHealthRecordToHealthRecordEntity(HealthRecord record) {
        if (record == null) return null;
        return HealthRecordEntity.builder()
                .id(record.getId())
                .date(record.getDate())
                .foodIntake(record.getFoodIntake())
                .waterIntake(record.getWaterIntake())
                .mood(record.getMood() != null ? MoodConverter.convertFromMoodToMoodEntity(record.getMood()) : null)
                .activityLevel(record.getActivityLevel())
                .socialInteraction(record.getSocialInteraction())
                .notes(record.getNotes())
                .build();
    }

    public static BreedHealthInfo convertFromBreedHealthInfoEntityToBreedHealthInfo(BreedHealthInfoEntity entity) {
        if (entity == null) return null;
        return BreedHealthInfo.builder()
                .breed(entity.getBreed() != null ? BreedConverter.convertFromBreedEntityToBreed(entity.getBreed()) : null)
                .ageRangeEnd(entity.getAgeRangeEnd())
                .ageRangeStart(entity.getAgeRangeStart())
                .normalFoodIntake(entity.getNormalFoodIntake())
                .normalWaterIntake(entity.getNormalWaterIntake())
                .build();
    }

    public static BreedHealthInfoEntity convertFromBreedHealthInfoToBreedHealthInfoEntity(BreedHealthInfo info) {
        if (info == null) return null;
        return BreedHealthInfoEntity.builder()
                .breed(info.getBreed() != null ? BreedConverter.convertFromBreedToBreedEntity(info.getBreed()) : null)
                .ageRangeEnd(info.getAgeRangeEnd())
                .ageRangeStart(info.getAgeRangeStart())
                .normalFoodIntake(info.getNormalFoodIntake())
                .normalWaterIntake(info.getNormalWaterIntake())
                .build();
    }

    public static List<BreedHealthInfoEntity> convertFromBreedHealthInfosToEntities(List<BreedHealthInfo> infos){
        List<BreedHealthInfoEntity> entities = new ArrayList<>();
        for (BreedHealthInfo info : infos) {
            entities.add(convertFromBreedHealthInfoToBreedHealthInfoEntity(info));
        }
        return entities;
    };

    public static List<BreedHealthInfo> convertFromEntitiesToBreedHealthInfos(List<BreedHealthInfoEntity> entities){
        List<BreedHealthInfo> infos = new ArrayList<>();
        for (BreedHealthInfoEntity entity : entities) {
            infos.add(convertFromBreedHealthInfoEntityToBreedHealthInfo(entity));
        }
        return infos;
    };
}
