package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.HealthRecordEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class HealthConverterTest {

    private static final HealthRecord healthRecord = HealthRecord.builder()
            .id(1L)
            .date(new java.util.Date())
            .foodIntake(1.5)
            .waterIntake(1.0)
            .activityLevel(10)
            .socialInteraction("Friendly")
            .notes("Good health")
            .build();

    private static final HealthRecordEntity healthRecordEntity = HealthRecordEntity.builder()
            .id(1L)
            .date(new java.util.Date())
            .foodIntake(1.5)
            .waterIntake(1.0)
            .activityLevel(10)
            .socialInteraction("Friendly")
            .notes("Good health")
            .build();

    private static final BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder()
            .ageRangeStart(1)
            .ageRangeEnd(5)
            .normalFoodIntake(2.5)
            .normalWaterIntake(1.5)
            .build();

    private static final BreedHealthInfoEntity breedHealthInfoEntity = BreedHealthInfoEntity.builder()
            .ageRangeStart(1)
            .ageRangeEnd(5)
            .normalFoodIntake(2.5)
            .normalWaterIntake(1.5)
            .build();

    // Tests for HealthRecordEntity to HealthRecord conversions

    @Test
    void convertFromHealthRecordEntityToHealthRecord_shouldConvertSuccessfully() {
        HealthRecord result = HealthConverter.convertFromHealthRecordEntityToHealthRecord(healthRecordEntity);

        assertNotNull(result);
        assertEquals(healthRecordEntity.getId(), result.getId());
        assertEquals(healthRecordEntity.getDate(), result.getDate());
        assertEquals(healthRecordEntity.getFoodIntake(), result.getFoodIntake());
        assertEquals(healthRecordEntity.getWaterIntake(), result.getWaterIntake());
        assertEquals(healthRecordEntity.getActivityLevel(), result.getActivityLevel());
        assertEquals(healthRecordEntity.getSocialInteraction(), result.getSocialInteraction());
        assertEquals(healthRecordEntity.getNotes(), result.getNotes());
    }

    @Test
    void convertFromHealthRecordEntityToHealthRecord_shouldReturnNullForNullEntity() {
        HealthRecord result = HealthConverter.convertFromHealthRecordEntityToHealthRecord(null);

        assertNull(result);
    }

    @Test
    void convertFromHealthRecordEntitiesToHealthRecords_shouldConvertListSuccessfully() {
        List<HealthRecordEntity> entities = List.of(healthRecordEntity);
        List<HealthRecord> result = HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(healthRecordEntity.getId(), result.get(0).getId());
    }

    @Test
    void convertFromHealthRecordEntitiesToHealthRecords_shouldReturnEmptyListForEmptyInput() {
        List<HealthRecord> result = HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromHealthRecordToHealthRecordEntity_shouldConvertSuccessfully() {
        HealthRecordEntity result = HealthConverter.convertFromHealthRecordToHealthRecordEntity(healthRecord);

        assertNotNull(result);
        assertEquals(healthRecord.getId(), result.getId());
        assertEquals(healthRecord.getDate(), result.getDate());
        assertEquals(healthRecord.getFoodIntake(), result.getFoodIntake());
        assertEquals(healthRecord.getWaterIntake(), result.getWaterIntake());
        assertEquals(healthRecord.getActivityLevel(), result.getActivityLevel());
        assertEquals(healthRecord.getSocialInteraction(), result.getSocialInteraction());
        assertEquals(healthRecord.getNotes(), result.getNotes());
    }

    @Test
    void convertFromHealthRecordToHealthRecordEntity_shouldReturnNullForNullHealthRecord() {
        HealthRecordEntity result = HealthConverter.convertFromHealthRecordToHealthRecordEntity(null);

        assertNull(result);
    }

    @Test
    void convertFromHealthRecordsToHealthRecordEntities_shouldConvertListSuccessfully() {
        List<HealthRecord> records = List.of(healthRecord);
        List<HealthRecordEntity> result = HealthConverter.convertFromHealthRecordsToHealthRecordEntities(records);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(healthRecord.getId(), result.get(0).getId());
    }

    // Tests for BreedHealthInfoEntity to BreedHealthInfo conversions

    @Test
    void convertFromBreedHealthInfoEntityToBreedHealthInfo_shouldConvertSuccessfully() {
        BreedHealthInfo result = HealthConverter.convertFromBreedHealthInfoEntityToBreedHealthInfo(breedHealthInfoEntity);

        assertNotNull(result);
        assertEquals(breedHealthInfoEntity.getAgeRangeStart(), result.getAgeRangeStart());
        assertEquals(breedHealthInfoEntity.getAgeRangeEnd(), result.getAgeRangeEnd());
        assertEquals(breedHealthInfoEntity.getNormalFoodIntake(), result.getNormalFoodIntake());
        assertEquals(breedHealthInfoEntity.getNormalWaterIntake(), result.getNormalWaterIntake());
    }

    @Test
    void convertFromBreedHealthInfoEntityToBreedHealthInfo_shouldReturnNullForNullEntity() {
        BreedHealthInfo result = HealthConverter.convertFromBreedHealthInfoEntityToBreedHealthInfo(null);

        assertNull(result);
    }

    @Test
    void convertFromBreedHealthInfoToBreedHealthInfoEntity_shouldConvertSuccessfully() {
        BreedHealthInfoEntity result = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoEntity(breedHealthInfo);

        assertNotNull(result);
        assertEquals(breedHealthInfo.getAgeRangeStart(), result.getAgeRangeStart());
        assertEquals(breedHealthInfo.getAgeRangeEnd(), result.getAgeRangeEnd());
        assertEquals(breedHealthInfo.getNormalFoodIntake(), result.getNormalFoodIntake());
        assertEquals(breedHealthInfo.getNormalWaterIntake(), result.getNormalWaterIntake());
    }

    @Test
    void convertFromBreedHealthInfoToBreedHealthInfoEntity_shouldReturnNullForNullInfo() {
        BreedHealthInfoEntity result = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoEntity(null);

        assertNull(result);
    }

    @Test
    void convertFromBreedHealthInfosToEntities_shouldConvertListSuccessfully() {
        List<BreedHealthInfo> infos = List.of(breedHealthInfo);
        List<BreedHealthInfoEntity> result = HealthConverter.convertFromBreedHealthInfosToEntities(infos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breedHealthInfo.getAgeRangeStart(), result.get(0).getAgeRangeStart());
    }

    @Test
    void convertFromBreedHealthInfosToEntities_shouldReturnEmptyListForEmptyInput() {
        List<BreedHealthInfoEntity> result = HealthConverter.convertFromBreedHealthInfosToEntities(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromEntitiesToBreedHealthInfos_shouldConvertListSuccessfully() {
        List<BreedHealthInfoEntity> entities = List.of(breedHealthInfoEntity);
        List<BreedHealthInfo> result = HealthConverter.convertFromEntitiesToBreedHealthInfos(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breedHealthInfoEntity.getAgeRangeStart(), result.get(0).getAgeRangeStart());
    }

    @Test
    void convertFromEntitiesToBreedHealthInfos_shouldReturnEmptyListForEmptyInput() {
        List<BreedHealthInfo> result = HealthConverter.convertFromEntitiesToBreedHealthInfos(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
