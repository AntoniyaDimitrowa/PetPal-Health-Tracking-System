package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.health.HealthRecordDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HealthConverter {

    public static List<HealthRecord> convertFromHealthRecordDTOsToHealthRecords(List<HealthRecordDTO> dtos) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        for (HealthRecordDTO dto : dtos) {
            result.add(convertFromHealthRecordDTOToHealthRecord(dto));
        }
        return result;
    }

    public static HealthRecord convertFromHealthRecordDTOToHealthRecord(HealthRecordDTO dto) {
        if (dto == null) return null;
        return HealthRecord.builder()
                .date(dto.getDate())
                .foodIntake(dto.getFoodIntake())
                .waterIntake(dto.getWaterIntake())
                .mood(MoodConverter.convertFromMoodDTOToMood(dto.getMood()))
                .activityLevel(dto.getActivityLevel())
                .socialInteraction(dto.getSocialInteraction())
                .notes(dto.getNotes())
                .build();
    }

    public static List<HealthRecordDTO> convertFromHealthRecordsToHealthRecordDTOs(List<HealthRecord> records) {
        ArrayList<HealthRecordDTO> result = new ArrayList<>();
        for (HealthRecord healthRecord : records) {
            result.add(convertFromHealthRecordToHealthRecordDTO(healthRecord));
        }
        return result;
    }

    public static HealthRecordDTO convertFromHealthRecordToHealthRecordDTO(HealthRecord healthRecord) {
        if (healthRecord == null) return null;
        return HealthRecordDTO.builder()
                .date(healthRecord.getDate())
                .foodIntake(healthRecord.getFoodIntake())
                .waterIntake(healthRecord.getWaterIntake())
                .mood(MoodConverter.convertFromMoodToMoodDTO(healthRecord.getMood()))
                .activityLevel(healthRecord.getActivityLevel())
                .socialInteraction(healthRecord.getSocialInteraction())
                .notes(healthRecord.getNotes())
                .build();
    }

    public static BreedHealthInfo convertFromBreedHealthInfoDTOToBreedHealthInfo(BreedHealthInfoDTO dto) {
        if (dto == null) return null;
        return BreedHealthInfo.builder()
                .ageRangeEnd(dto.getAgeRangeEnd())
                .ageRangeStart(dto.getAgeRangeStart())
                .normalFoodIntake(dto.getNormalFoodIntake())
                .normalWaterIntake(dto.getNormalWaterIntake())
                .build();
    }

    public static BreedHealthInfoDTO convertFromBreedHealthInfoToBreedHealthInfoDTO(BreedHealthInfo info) {
        if (info == null) return null;
        return BreedHealthInfoDTO.builder()
                .ageRangeEnd(info.getAgeRangeEnd())
                .ageRangeStart(info.getAgeRangeStart())
                .normalFoodIntake(info.getNormalFoodIntake())
                .normalWaterIntake(info.getNormalWaterIntake())
                .build();
    }

    public static List<BreedHealthInfoDTO> convertFromBreedHealthInfosToDTOs(List<BreedHealthInfo> infos) {
        ArrayList<BreedHealthInfoDTO> entities = new ArrayList<>();
        for (BreedHealthInfo info : infos) {
            entities.add(convertFromBreedHealthInfoToBreedHealthInfoDTO(info));
        }
        return entities;
    }

    public static List<BreedHealthInfo> convertFromDTOsToBreedHealthInfos(List<BreedHealthInfoDTO> dtos) {
        ArrayList<BreedHealthInfo> infos = new ArrayList<>();
        for (BreedHealthInfoDTO dto : dtos) {
            infos.add(convertFromBreedHealthInfoDTOToBreedHealthInfo(dto));
        }
        return infos;
    }
}
