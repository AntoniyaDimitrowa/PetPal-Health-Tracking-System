package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.controller.dto.BreedDTO;
import com.example.petpal.controller.dto.PetDTO;

public class PetConverter {

    private PetConverter(){}

    public static PetDTO convertFromPetToPetDTO(Pet pet){
        return PetDTO.builder()
                .name(pet.getName())
                .breed(BreedConverter.convertFromBreedToBreedDTO(pet.getBreed()))
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsDTOs(pet.getVaccinationRecords()))
                .build();
    };
}
