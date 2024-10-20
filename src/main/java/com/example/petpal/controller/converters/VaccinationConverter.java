package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;

import java.util.ArrayList;

public class VaccinationConverter {
    private VaccinationConverter(){}

    public static ArrayList<VaccinationRecord> convertFromVaccinationRecordDTOsToVaccinationRecords(ArrayList<VaccinationRecordDTO> dtos){
        ArrayList<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordDTO dto : dtos) {
            result.add(convertFromVaccinationRecordDTOtoVaccinationRecord(dto));
        }
        return result;
    };

    public static VaccinationRecord convertFromVaccinationRecordDTOtoVaccinationRecord(VaccinationRecordDTO dto){
        if (dto == null) return null;
        return VaccinationRecord.builder()
                .vaccination(convertFromVaccinationDTOtoVaccination(dto.getVaccination()))
                .date(dto.getDate())
                .build();
    };

    public static Vaccination convertFromVaccinationDTOtoVaccination(VaccinationDTO dto){
        if (dto == null) return null;
        return Vaccination.builder()
                .name(dto.getName())
                .type(dto.getType())
                .range(dto.getRange())
                .build();
    };

    public static ArrayList<VaccinationRecordDTO> convertFromVaccinationRecordsToVaccinationRecordsDTOs(ArrayList<VaccinationRecord> records){
        ArrayList<VaccinationRecordDTO> result = new ArrayList<>();

        for (VaccinationRecord vr : records) {
            result.add(convertFromVaccinationRecordToVaccinationRecordDTO(vr));
        }
        return result;
    };

    public static ArrayList<VaccinationDTO> convertFromVaccinationsToVaccinationsDTOs(ArrayList<Vaccination> vaccinations){
        ArrayList<VaccinationDTO> result = new ArrayList<>();

        for (Vaccination v : vaccinations) {
            result.add(convertFromVaccinationToVaccinationDTO(v));
        }
        return result;
    };

    public static VaccinationRecordDTO convertFromVaccinationRecordToVaccinationRecordDTO(VaccinationRecord record){
        if (record == null) return null;
        return VaccinationRecordDTO.builder()
                .vaccination(convertFromVaccinationToVaccinationDTO(record.getVaccination()))
                .date(record.getDate())
                .build();
    };

    public static VaccinationDTO convertFromVaccinationToVaccinationDTO(Vaccination vaccination){
        if (vaccination == null) return null;
        return VaccinationDTO.builder()
                .id(vaccination.getId())
                .name(vaccination.getName())
                .type(vaccination.getType())
                .range(vaccination.getRange())
                .build();
    };
}
