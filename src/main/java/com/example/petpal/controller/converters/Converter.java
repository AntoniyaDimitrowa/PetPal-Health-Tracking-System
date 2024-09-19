package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.controller.dto.BreedDTO;
import com.example.petpal.controller.dto.VaccinationDTO;
import com.example.petpal.controller.dto.VaccinationRecordDTO;

import java.util.ArrayList;

public abstract class Converter {
    private Converter(){};

    public static Breed convertFromBreedDTOtoBreed(BreedDTO dto){
        return new Breed(dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getNormalMood(),
                dto.getMinimumExercisePerDay(),
                dto.getCommonHealthProblems());
    };

    public static ArrayList<VaccinationRecord> convertFromVaccinationRecordDTOsToVaccinationRecords(ArrayList<VaccinationRecordDTO> dtos){
        ArrayList<VaccinationRecord> result = new ArrayList<>();

        for (VaccinationRecordDTO dto : dtos) {
            result.add(convertFromVaccinationRecordDTOtoVaccinationRecord(dto));
        }
        return result;
    };

    public static VaccinationRecord convertFromVaccinationRecordDTOtoVaccinationRecord(VaccinationRecordDTO dto){
        return new VaccinationRecord(dto.getId(),
                convertFromVaccinationDTOtoVaccination(dto.getVaccination()),
                dto.getDate()
        );
    };

    public static Vaccination convertFromVaccinationDTOtoVaccination(VaccinationDTO dto){
        return new Vaccination(dto.getId(),
                dto.getName(),
                dto.getType(),
                dto.getRange()
        );
    };
}
