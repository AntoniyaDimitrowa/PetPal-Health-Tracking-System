package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;

import com.example.petpal.persistence.entity.VaccinationEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;

import java.util.ArrayList;

public class VaccinationConverter {
    private VaccinationConverter(){}

    public static ArrayList<VaccinationRecord> convertFromVaccinationRecordEntitiesToVaccinationRecords(ArrayList<VaccinationRecordEntity> entities){
        ArrayList<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordEntity entity : entities) {
            result.add(convertFromVaccinationRecordEntitytoVaccinationRecord(entity));
        }
        return result;
    };

    public static VaccinationRecord convertFromVaccinationRecordEntitytoVaccinationRecord(VaccinationRecordEntity entity){
        return new VaccinationRecord(entity.getId(),
                convertFromVaccinationEntitytoVaccination(entity.getVaccination()),
                entity.getDate()
        );
    };

    public static Vaccination convertFromVaccinationEntitytoVaccination(VaccinationEntity entity){
        return new Vaccination(entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getRange()
        );
    };

    public static ArrayList<VaccinationRecordEntity> convertFromVaccinationRecordsToVaccinationRecordsEntities(ArrayList<VaccinationRecord> records){
        ArrayList<VaccinationRecordEntity> result = new ArrayList<>();

        for (VaccinationRecord vr : records) {
            result.add(convertFromVaccinationRecordToVaccinationRecordEntity(vr));
        }
        return result;
    };

    public static VaccinationRecordEntity convertFromVaccinationRecordToVaccinationRecordEntity(VaccinationRecord record){
        return VaccinationRecordEntity.builder()
                .id(record.getId())
                .vaccination(convertFromVaccinationToVaccinationEntity(record.getVaccination()))
                .date(record.getDate())
                .build();
    };

    public static VaccinationEntity convertFromVaccinationToVaccinationEntity(Vaccination vaccination){
        return VaccinationEntity.builder()
                .id(vaccination.getId())
                .name(vaccination.getName())
                .type(vaccination.getType())
                .range(vaccination.getRange())
                .build();
    };
}
