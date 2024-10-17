package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;

import java.util.ArrayList;

public class PetConverter {

    private PetConverter(){}

    public static PetDTO convertFromPetToPetDTO(Pet pet){
        return PetDTO.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(BreedConverter.convertFromBreedToBreedDTO(pet.getBreed()))
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsDTOs(pet.getVaccinationRecords()))
                .healthRecords(HealthConverter.convertFromHealthRecordsToHealthRecordDTOs(pet.getHealthRecords()))
                .build();
    };

    public static Pet convertFromPetDTOToPet(PetDTO pet){
        return Pet.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(BreedConverter.convertFromBreedDTOToBreed(pet.getBreed()))
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordDTOsToVaccinationRecords(pet.getVaccinationRecords()))
                .healthRecords(HealthConverter.convertFromHealthRecordDTOsToHealthRecords(pet.getHealthRecords()))
                .build();
    };

    public static Pet convertFromCreatePetDTOToPet(CreatePetDTO pet){
        return Pet.builder()
                .name(pet.getName())
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .build();
    };

    public static ArrayList<PetDTO> convertFromPetsToPetDTOs(ArrayList<Pet> pets){
        ArrayList<PetDTO> dtos = new ArrayList<>();
        for (Pet pet : pets) {
            dtos.add(convertFromPetToPetDTO(pet));
        }
        return dtos;
    };

    public static ArrayList<Pet> convertFromPetDTOsToPets(ArrayList<PetDTO> dtos){
        ArrayList<Pet> pets = new ArrayList<>();
        for (PetDTO dto : dtos) {
            pets.add(convertFromPetDTOToPet(dto));
        }
        return pets;
    };
}
