package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class HealthConverter {

    public static ArrayList<HealthRecord> convertFromHealthRecordEntitiesToHealthRecords(List<HealthRecordEntity> entities) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        for (HealthRecordEntity entity : entities) {
            result.add(convertFromHealthRecordEntityToHealthRecord(entity));
        }
        return result;
    }

    public static HealthRecord convertFromHealthRecordEntityToHealthRecord(HealthRecordEntity entity) {
        return HealthRecord.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .foodIntake(entity.getFoodIntake())
                .waterIntake(entity.getWaterIntake())
                .mood(MoodConverter.convertFromMoodEntityToMood(entity.getMood()))
                .activityLevel(entity.getActivityLevel())
                .socialInteraction(entity.getSocialInteraction())
                .notes(entity.getNotes())
                .build();
    }

    public static ArrayList<HealthRecordEntity> convertFromHealthRecordsToHealthRecordEntities(List<HealthRecord> records) {
        ArrayList<HealthRecordEntity> result = new ArrayList<>();
        for (HealthRecord record : records) {
            result.add(convertFromHealthRecordToHealthRecordEntity(record));
        }
        return result;
    }

    public static HealthRecordEntity convertFromHealthRecordToHealthRecordEntity(HealthRecord record) {
        return HealthRecordEntity.builder()
                .id(record.getId())
                .date(record.getDate())
                .foodIntake(record.getFoodIntake())
                .waterIntake(record.getWaterIntake())
                .mood(MoodConverter.convertFromMoodToMoodEntity(record.getMood()))
                .activityLevel(record.getActivityLevel())
                .socialInteraction(record.getSocialInteraction())
                .notes(record.getNotes())
                .build();
    }

    public static BreedHealthInfo convertFromBreedHealthInfoEntityToBreedHealthInfo(BreedHealthInfoEntity entity) {
        return BreedHealthInfo.builder()
                .breed(BreedConverter.convertFromBreedEntityToBreed(entity.getBreed()))
                .ageRangeEnd(entity.getAgeRangeEnd())
                .ageRangeStart(entity.getAgeRangeStart())
                .normalFoodIntake(entity.getNormalFoodIntake())
                .normalWaterIntake(entity.getNormalWaterIntake())
                .build();
    }

    public static BreedHealthInfoEntity convertFromBreedHealthInfoToBreedHealthInfoEntity(BreedHealthInfo info) {
        return BreedHealthInfoEntity.builder()
                .breed(BreedConverter.convertFromBreedToBreedEntity(info.getBreed()))
                .ageRangeEnd(info.getAgeRangeEnd())
                .ageRangeStart(info.getAgeRangeStart())
                .normalFoodIntake(info.getNormalFoodIntake())
                .normalWaterIntake(info.getNormalWaterIntake())
                .build();
    }

    public static ArrayList<BreedHealthInfoEntity> convertFromBreedHealthInfosToEntities(ArrayList<BreedHealthInfo> infos){
        ArrayList<BreedHealthInfoEntity> entities = new ArrayList<>();
        for (BreedHealthInfo info : infos) {
            entities.add(convertFromBreedHealthInfoToBreedHealthInfoEntity(info));
        }
        return entities;
    };

    public static ArrayList<BreedHealthInfo> convertFromEntitiesToBreedHealthInfos(ArrayList<BreedHealthInfoEntity> entities){
        ArrayList<BreedHealthInfo> infos = new ArrayList<>();
        for (BreedHealthInfoEntity entity : entities) {
            infos.add(convertFromBreedHealthInfoEntityToBreedHealthInfo(entity));
        }
        return infos;
    };
}
