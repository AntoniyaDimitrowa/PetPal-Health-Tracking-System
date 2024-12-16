package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        for (HealthRecord healthRecord : records) {
            result.add(convertFromHealthRecordToHealthRecordEntity(healthRecord));
        }
        return result;
    }

    public static HealthRecordEntity convertFromHealthRecordToHealthRecordEntity(HealthRecord healthRecord) {
        if (healthRecord == null) return null;
        return HealthRecordEntity.builder()
                .id(healthRecord.getId())
                .date(healthRecord.getDate())
                .foodIntake(healthRecord.getFoodIntake())
                .waterIntake(healthRecord.getWaterIntake())
                .mood(healthRecord.getMood() != null ? MoodConverter.convertFromMoodToMoodEntity(healthRecord.getMood()) : null)
                .activityLevel(healthRecord.getActivityLevel())
                .socialInteraction(healthRecord.getSocialInteraction())
                .notes(healthRecord.getNotes())
                .build();
    }

    public static BreedHealthInfo convertFromBreedHealthInfoEntityToBreedHealthInfo(BreedHealthInfoEntity entity) {
        if (entity == null) return null;
        return BreedHealthInfo.builder()
                .id(entity.getId())
                .breed(entity.getBreed() != null ? BreedConverter.convertFromBreedEntityToBreed(entity.getBreed()) : null)
                .ageRangeEnd(entity.getAgeRangeEnd())
                .ageRangeStart(entity.getAgeRangeStart())
                .normalFoodIntake(entity.getNormalFoodIntake())
                .normalWaterIntake(entity.getNormalWaterIntake())
                .weightRangeMin(entity.getWeightRangeMin())
                .weightRangeMax(entity.getWeightRangeMax())
                .build();
    }

    public static BreedHealthInfoEntity convertFromBreedHealthInfoToBreedHealthInfoEntity(BreedHealthInfo info) {
        if (info == null) return null;
        return BreedHealthInfoEntity.builder()
                .id(info.getId())
                .breed(info.getBreed() != null ? BreedConverter.convertFromBreedToBreedEntity(info.getBreed()) : null)
                .ageRangeEnd(info.getAgeRangeEnd())
                .ageRangeStart(info.getAgeRangeStart())
                .normalFoodIntake(info.getNormalFoodIntake())
                .normalWaterIntake(info.getNormalWaterIntake())
                .weightRangeMin(info.getWeightRangeMin())
                .weightRangeMax(info.getWeightRangeMax())
                .build();
    }

    public static List<BreedHealthInfoEntity> convertFromBreedHealthInfosToEntities(List<BreedHealthInfo> infos){
        List<BreedHealthInfoEntity> entities = new ArrayList<>();
        for (BreedHealthInfo info : infos) {
            entities.add(convertFromBreedHealthInfoToBreedHealthInfoEntity(info));
        }
        return entities;
    }

    public static List<BreedHealthInfo> convertFromEntitiesToBreedHealthInfos(List<BreedHealthInfoEntity> entities){
        List<BreedHealthInfo> infos = new ArrayList<>();
        for (BreedHealthInfoEntity entity : entities) {
            infos.add(convertFromBreedHealthInfoEntityToBreedHealthInfo(entity));
        }
        return infos;
    }
}
