package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;

import com.example.petpal.persistence.entity.VaccinationEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VaccinationConverter {

    public static List<VaccinationRecord> convertFromVaccinationRecordEntitiesToVaccinationRecords(List<VaccinationRecordEntity> entities){
        if(entities == null) return new ArrayList<>();

        List<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordEntity entity : entities) {
            result.add(convertFromVaccinationRecordEntityToVaccinationRecord(entity));
        }
        return result;
    }

    public static List<Vaccination> convertFromVaccinationEntitiesToVaccination(List<VaccinationEntity> entities){
        if(entities == null) return new ArrayList<>();
        List<Vaccination> result = new ArrayList<>();

        for (VaccinationEntity entity : entities) {
            result.add(convertFromVaccinationEntityToVaccination(entity));
        }
        return result;
    }

    public static VaccinationRecord convertFromVaccinationRecordEntityToVaccinationRecord(VaccinationRecordEntity entity){
        if(entity == null) return null;

        return VaccinationRecord.builder()
                .id(entity.getId())
                .vaccination(entity.getVaccination() != null ? convertFromVaccinationEntityToVaccination(entity.getVaccination()) : null)
                .date(entity.getDate())
                .build();
    }

    public static Vaccination convertFromVaccinationEntityToVaccination(VaccinationEntity entity){
        if(entity == null) return null;

        return new Vaccination(entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getRange()
        );
    }
    public static List<VaccinationRecordEntity> convertFromVaccinationRecordsToVaccinationRecordsEntities(List<VaccinationRecord> records){
        if(records == null) return new ArrayList<>();

        List<VaccinationRecordEntity> result = new ArrayList<>();

        for (VaccinationRecord vr : records) {
            result.add(convertFromVaccinationRecordToVaccinationRecordEntity(vr));
        }
        return result;
    }

    public static VaccinationRecordEntity convertFromVaccinationRecordToVaccinationRecordEntity(VaccinationRecord vaccinationRecord){
        if (vaccinationRecord == null) return null;
        return VaccinationRecordEntity.builder()
                .id(vaccinationRecord.getId())
                .vaccination(vaccinationRecord.getVaccination() != null ? convertFromVaccinationToVaccinationEntity(vaccinationRecord.getVaccination()) : null)
                .date(vaccinationRecord.getDate())
                .build();
    }

    public static VaccinationEntity convertFromVaccinationToVaccinationEntity(Vaccination vaccination){
        if(vaccination == null) return null;

        return VaccinationEntity.builder()
                .id(vaccination.getId())
                .name(vaccination.getName())
                .type(vaccination.getType())
                .range(vaccination.getRange())
                .build();
    }
}
