package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.controller.dto.PetDTO;
import com.example.petpal.persistence.entity.PetEntity;

public class PetConverter {

    private PetConverter(){}

    public static PetEntity convertFromPetToPetEntity(Pet pet){
        return PetEntity.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(BreedConverter.convertFromBreedToBreedEntity(pet.getBreed()))
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsEntities(pet.getVaccinationRecords()))
                .build();
    };

    public static Pet convertFromPetEntityToPet(PetEntity pet){
        return Pet.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(BreedConverter.convertFromBreedEntityToBreed(pet.getBreed()))
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(pet.getVaccinationRecords()))
                .build();
    };
}
