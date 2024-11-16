package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.controller.dto.vaccination.CreateVaccinationRecordDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class VaccinationConverter {

    public static List<VaccinationRecord> convertFromVaccinationRecordDTOsToVaccinationRecords(List<VaccinationRecordDTO> dtos){
        if(dtos == null) return new ArrayList<>();
        ArrayList<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordDTO dto : dtos) {
            result.add(convertFromVaccinationRecordDTOtoVaccinationRecord(dto));
        }
        return result;
    }

    public static VaccinationRecord convertFromVaccinationRecordDTOtoVaccinationRecord(VaccinationRecordDTO dto){
        if (dto == null) return null;
        return VaccinationRecord.builder()
                .vaccination(convertFromVaccinationDTOtoVaccination(dto.getVaccination()))
                .date(dto.getDate())
                .build();
    }

    public static Vaccination convertFromVaccinationDTOtoVaccination(VaccinationDTO dto){
        if (dto == null) return null;
        return Vaccination.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .range(dto.getRange())
                .build();
    }

    public static List<VaccinationRecordDTO> convertFromVaccinationRecordsToVaccinationRecordsDTOs(List<VaccinationRecord> records){
        if(records == null) return new ArrayList<>();
        ArrayList<VaccinationRecordDTO> result = new ArrayList<>();

        for (VaccinationRecord vr : records) {
            result.add(convertFromVaccinationRecordToVaccinationRecordDTO(vr));
        }
        return result;
    }

    public static List<VaccinationDTO> convertFromVaccinationsToVaccinationsDTOs(List<Vaccination> vaccinations){
        if(vaccinations == null) return new ArrayList<>();
        ArrayList<VaccinationDTO> result = new ArrayList<>();

        for (Vaccination v : vaccinations) {
            result.add(convertFromVaccinationToVaccinationDTO(v));
        }
        return result;
    }

    public static VaccinationRecordDTO convertFromVaccinationRecordToVaccinationRecordDTO(VaccinationRecord vaccinationRecord){
        if (vaccinationRecord == null) return null;
        return VaccinationRecordDTO.builder()
                .vaccination(convertFromVaccinationToVaccinationDTO(vaccinationRecord.getVaccination()))
                .date(vaccinationRecord.getDate())
                .build();
    }

    public static VaccinationDTO convertFromVaccinationToVaccinationDTO(Vaccination vaccination){
        if (vaccination == null) return null;
        return VaccinationDTO.builder()
                .id(vaccination.getId())
                .name(vaccination.getName())
                .type(vaccination.getType())
                .range(vaccination.getRange())
                .build();
    }
}
