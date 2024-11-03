package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;

import com.example.petpal.persistence.entity.VaccinationEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;

import java.util.ArrayList;
import java.util.List;

public class VaccinationConverter {
    private VaccinationConverter(){}

    public static List<VaccinationRecord> convertFromVaccinationRecordEntitiesToVaccinationRecords(List<VaccinationRecordEntity> entities){
        List<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordEntity entity : entities) {
            result.add(convertFromVaccinationRecordEntityToVaccinationRecord(entity));
        }
        return result;
    };

    public static List<Vaccination> convertFromVaccinationEntitiesToVaccination(List<VaccinationEntity> entities){
        List<Vaccination> result = new ArrayList<>();

        for (VaccinationEntity entity : entities) {
            result.add(convertFromVaccinationEntityToVaccination(entity));
        }
        return result;
    };

    public static VaccinationRecord convertFromVaccinationRecordEntityToVaccinationRecord(VaccinationRecordEntity entity){
        return VaccinationRecord.builder()
                .id(entity.getId())
                .vaccination(entity.getVaccination() != null ? convertFromVaccinationEntityToVaccination(entity.getVaccination()) : null)
                .date(entity.getDate())
                .build();
    };

    public static Vaccination convertFromVaccinationEntityToVaccination(VaccinationEntity entity){
        return new Vaccination(entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getRange()
        );
    };

    public static List<VaccinationRecordEntity> convertFromVaccinationRecordsToVaccinationRecordsEntities(List<VaccinationRecord> records){
        List<VaccinationRecordEntity> result = new ArrayList<>();

        for (VaccinationRecord vr : records) {
            result.add(convertFromVaccinationRecordToVaccinationRecordEntity(vr));
        }
        return result;
    };

    public static VaccinationRecordEntity convertFromVaccinationRecordToVaccinationRecordEntity(VaccinationRecord record){
        if (record == null) return null;
        return VaccinationRecordEntity.builder()
                .id(record.getId())
                .vaccination(record.getVaccination() != null ? convertFromVaccinationToVaccinationEntity(record.getVaccination()) : null)
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
