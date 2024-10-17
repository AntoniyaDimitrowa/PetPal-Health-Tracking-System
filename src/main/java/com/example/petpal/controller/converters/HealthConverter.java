package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.health.HealthRecordDTO;

import java.util.ArrayList;
import java.util.List;

public class HealthConverter {

    public static ArrayList<HealthRecord> convertFromHealthRecordDTOsToHealthRecords(List<HealthRecordDTO> dtos) {
        ArrayList<HealthRecord> result = new ArrayList<>();
        for (HealthRecordDTO dto : dtos) {
            result.add(convertFromHealthRecordDTOToHealthRecord(dto));
        }
        return result;
    }

    public static HealthRecord convertFromHealthRecordDTOToHealthRecord(HealthRecordDTO dto) {
        return HealthRecord.builder()
                .date(dto.getDate())
                .foodIntake(dto.getFoodIntake())
                .waterIntake(dto.getWaterIntake())
                .mood(dto.getMood())
                .behavior(dto.getBehavior())
                .notes(dto.getNotes())
                .build();
    }

    public static ArrayList<HealthRecordDTO> convertFromHealthRecordsToHealthRecordDTOs(List<HealthRecord> records) {
        ArrayList<HealthRecordDTO> result = new ArrayList<>();
        for (HealthRecord record : records) {
            result.add(convertFromHealthRecordToHealthRecordDTO(record));
        }
        return result;
    }

    public static HealthRecordDTO convertFromHealthRecordToHealthRecordDTO(HealthRecord record) {
        return HealthRecordDTO.builder()
                .date(record.getDate())
                .foodIntake(record.getFoodIntake())
                .waterIntake(record.getWaterIntake())
                .mood(record.getMood())
                .behavior(record.getBehavior())
                .notes(record.getNotes())
                .build();
    }

    public static BreedHealthInfo convertFromBreedHealthInfoDTOToBreedHealthInfo(BreedHealthInfoDTO dto) {
        return BreedHealthInfo.builder()
                //.breed(BreedConverter.convertFromBreedDTOToBreed(dto.getBreed()))
                .ageRangeEnd(dto.getAgeRangeEnd())
                .ageRangeStart(dto.getAgeRangeStart())
                .normalFoodIntake(dto.getNormalFoodIntake())
                .normalWaterIntake(dto.getNormalWaterIntake())
                .build();
    }

    public static BreedHealthInfoDTO convertFromBreedHealthInfoToBreedHealthInfoDTO(BreedHealthInfo info) {
        return BreedHealthInfoDTO.builder()
                //.breed(BreedConverter.convertFromBreedToBreedDTO(info.getBreed()))
                .ageRangeEnd(info.getAgeRangeEnd())
                .ageRangeStart(info.getAgeRangeStart())
                .normalFoodIntake(info.getNormalFoodIntake())
                .normalWaterIntake(info.getNormalWaterIntake())
                .build();
    }

    public static ArrayList<BreedHealthInfoDTO> convertFromBreedHealthInfosToDTOs(ArrayList<BreedHealthInfo> infos){
        ArrayList<BreedHealthInfoDTO> entities = new ArrayList<>();
        for (BreedHealthInfo info : infos) {
            entities.add(convertFromBreedHealthInfoToBreedHealthInfoDTO(info));
        }
        return entities;
    };

    public static ArrayList<BreedHealthInfo> convertFromDTOsToBreedHealthInfos(ArrayList<BreedHealthInfoDTO> dtos){
        ArrayList<BreedHealthInfo> infos = new ArrayList<>();
        for (BreedHealthInfoDTO dto : dtos) {
            infos.add(convertFromBreedHealthInfoDTOToBreedHealthInfo(dto));
        }
        return infos;
    };
}
