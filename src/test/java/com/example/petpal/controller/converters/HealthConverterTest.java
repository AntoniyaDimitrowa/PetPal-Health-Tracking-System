package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.health.HealthRecordDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthConverterTest {

    private static final Mood mood = Mood.builder().id(1L).name("Happy").emoji("😊").build();

    private static final HealthRecord healthRecord = HealthRecord.builder()
            .date(new Date())
            .foodIntake(1.5)
            .waterIntake(1.0)
            .mood(mood)
            .activityLevel(10)
            .socialInteraction("Friendly")
            .notes("Good health")
            .build();

    private static final HealthRecordDTO healthRecordDTO = HealthRecordDTO.builder()
            .date(new Date())
            .foodIntake(1.5)
            .waterIntake(1.0)
            .mood(MoodConverter.convertFromMoodToMoodDTO(mood))
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

    private static final BreedHealthInfoDTO breedHealthInfoDTO = BreedHealthInfoDTO.builder()
            .ageRangeStart(1)
            .ageRangeEnd(5)
            .normalFoodIntake(2.5)
            .normalWaterIntake(1.5)
            .build();

    // Tests for HealthRecord conversions
    @Test
    void convertFromHealthRecordDTOToHealthRecord_shouldConvertSuccessfully() {
        HealthRecord result = HealthConverter.convertFromHealthRecordDTOToHealthRecord(healthRecordDTO);

        assertNotNull(result);
        assertEquals(healthRecordDTO.getDate(), result.getDate());
        assertEquals(healthRecordDTO.getFoodIntake(), result.getFoodIntake());
        assertEquals(healthRecordDTO.getWaterIntake(), result.getWaterIntake());
        assertEquals(healthRecordDTO.getMood().getId(), result.getMood().getId());
        assertEquals(healthRecordDTO.getActivityLevel(), result.getActivityLevel());
        assertEquals(healthRecordDTO.getSocialInteraction(), result.getSocialInteraction());
        assertEquals(healthRecordDTO.getNotes(), result.getNotes());
    }

    @Test
    void convertFromHealthRecordToHealthRecordDTO_shouldConvertSuccessfully() {
        HealthRecordDTO result = HealthConverter.convertFromHealthRecordToHealthRecordDTO(healthRecord);

        assertNotNull(result);
        assertEquals(healthRecord.getDate(), result.getDate());
        assertEquals(healthRecord.getFoodIntake(), result.getFoodIntake());
        assertEquals(healthRecord.getWaterIntake(), result.getWaterIntake());
        assertEquals(healthRecord.getMood().getId(), result.getMood().getId());
        assertEquals(healthRecord.getActivityLevel(), result.getActivityLevel());
        assertEquals(healthRecord.getSocialInteraction(), result.getSocialInteraction());
        assertEquals(healthRecord.getNotes(), result.getNotes());
    }

    @Test
    void convertFromHealthRecordDTOsToHealthRecords_shouldConvertListSuccessfully() {
        List<HealthRecordDTO> dtos = List.of(healthRecordDTO);
        List<HealthRecord> result = HealthConverter.convertFromHealthRecordDTOsToHealthRecords(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(healthRecordDTO.getDate(), result.get(0).getDate());
    }

    @Test
    void convertFromHealthRecordsToHealthRecordDTOs_shouldConvertListSuccessfully() {
        List<HealthRecord> records = List.of(healthRecord);
        List<HealthRecordDTO> result = HealthConverter.convertFromHealthRecordsToHealthRecordDTOs(records);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(healthRecord.getDate(), result.get(0).getDate());
    }

    // Tests for BreedHealthInfo conversions
    @Test
    void convertFromBreedHealthInfoToBreedHealthInfoDTO_shouldConvertSuccessfully() {
        BreedHealthInfoDTO result = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoDTO(breedHealthInfo);

        assertNotNull(result);
        assertEquals(breedHealthInfo.getAgeRangeStart(), result.getAgeRangeStart());
        assertEquals(breedHealthInfo.getAgeRangeEnd(), result.getAgeRangeEnd());
        assertEquals(breedHealthInfo.getNormalFoodIntake(), result.getNormalFoodIntake());
        assertEquals(breedHealthInfo.getNormalWaterIntake(), result.getNormalWaterIntake());
    }

    @Test
    void convertFromBreedHealthInfoDTOToBreedHealthInfo_shouldConvertSuccessfully() {
        BreedHealthInfo result = HealthConverter.convertFromBreedHealthInfoDTOToBreedHealthInfo(breedHealthInfoDTO);

        assertNotNull(result);
        assertEquals(breedHealthInfoDTO.getAgeRangeStart(), result.getAgeRangeStart());
        assertEquals(breedHealthInfoDTO.getAgeRangeEnd(), result.getAgeRangeEnd());
        assertEquals(breedHealthInfoDTO.getNormalFoodIntake(), result.getNormalFoodIntake());
        assertEquals(breedHealthInfoDTO.getNormalWaterIntake(), result.getNormalWaterIntake());
    }

    // Null input tests
    @Test
    void convertFromHealthRecordDTOToHealthRecord_shouldReturnNullWhenDTOIsNull() {
        HealthRecord result = HealthConverter.convertFromHealthRecordDTOToHealthRecord(null);
        assertNull(result);
    }

    @Test
    void convertFromHealthRecordToHealthRecordDTO_shouldReturnNullWhenHealthRecordIsNull() {
        HealthRecordDTO result = HealthConverter.convertFromHealthRecordToHealthRecordDTO(null);
        assertNull(result);
    }

    @Test
    void convertFromBreedHealthInfoDTOToBreedHealthInfo_shouldReturnNullWhenDTOIsNull() {
        BreedHealthInfo result = HealthConverter.convertFromBreedHealthInfoDTOToBreedHealthInfo(null);
        assertNull(result);
    }

    @Test
    void convertFromBreedHealthInfoToBreedHealthInfoDTO_shouldReturnNullWhenInfoIsNull() {
        BreedHealthInfoDTO result = HealthConverter.convertFromBreedHealthInfoToBreedHealthInfoDTO(null);
        assertNull(result);
    }

    // List conversion tests for BreedHealthInfo
    @Test
    void convertFromBreedHealthInfosToDTOs_shouldConvertListSuccessfully() {
        List<BreedHealthInfo> infos = List.of(breedHealthInfo);
        List<BreedHealthInfoDTO> result = HealthConverter.convertFromBreedHealthInfosToDTOs(infos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breedHealthInfo.getAgeRangeStart(), result.get(0).getAgeRangeStart());
    }

    @Test
    void convertFromDTOsToBreedHealthInfos_shouldConvertListSuccessfully() {
        List<BreedHealthInfoDTO> dtos = List.of(breedHealthInfoDTO);
        List<BreedHealthInfo> result = HealthConverter.convertFromDTOsToBreedHealthInfos(dtos);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breedHealthInfoDTO.getAgeRangeStart(), result.get(0).getAgeRangeStart());
    }
}
