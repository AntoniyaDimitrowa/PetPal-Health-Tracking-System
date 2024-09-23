package com.example.petpal.business.converters;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.HealthRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class HealthRecordConverter {

    // Convert a list of HealthRecordEntity objects to HealthRecord objects
    public static ArrayList<HealthRecord> convertFromHealthRecordEntitiesToHealthRecords(List<HealthRecordEntity> entities) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        for (HealthRecordEntity entity : entities) {
            result.add(convertFromHealthRecordEntityToHealthRecord(entity));
        }
        return result;
    }

    // Convert a single HealthRecordEntity to HealthRecord
    public static HealthRecord convertFromHealthRecordEntityToHealthRecord(HealthRecordEntity entity) {
        return new HealthRecord(
                entity.getId(),
                entity.getDate(),
                entity.getFoodIntake(),
                entity.getWaterIntake(),
                entity.getMood(),
                entity.getBehavior(),
                entity.getNotes()
        );
    }

    // Convert a list of HealthRecord objects to HealthRecordEntity objects
    public static ArrayList<HealthRecordEntity> convertFromHealthRecordsToHealthRecordEntities(List<HealthRecord> records) {
        ArrayList<HealthRecordEntity> result = new ArrayList<>();
        for (HealthRecord record : records) {
            result.add(convertFromHealthRecordToHealthRecordEntity(record));
        }
        return result;
    }

    // Convert a single HealthRecord to HealthRecordEntity
    public static HealthRecordEntity convertFromHealthRecordToHealthRecordEntity(HealthRecord record) {
        return HealthRecordEntity.builder()
                .id(record.getId())
                .date(record.getDate())
                .foodIntake(record.getFoodIntake())
                .waterIntake(record.getWaterIntake())
                .mood(record.getMood())
                .behavior(record.getBehavior())
                .notes(record.getNotes())
                .build();
    }
}
