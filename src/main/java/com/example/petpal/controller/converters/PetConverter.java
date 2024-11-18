package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import com.example.petpal.controller.dto.pet.UpdatePetDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetConverter {

    public static PetDTO convertFromPetToPetDTO(Pet pet) {
        if (pet == null) return null;
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
    }

    public static Pet convertFromPetDTOToPet(PetDTO pet) {
        if (pet == null) return null;
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
    }

    public static Pet convertFromCreatePetDTOToPet(CreatePetDTO petDTO) {
        if (petDTO == null) return null;
        return Pet.builder()
                .name(petDTO.getName())
                .gender(petDTO.getGender())
                .birthdate(petDTO.getBirthdate())
                .weight(petDTO.getWeight())
                .image(petDTO.getImage())
                .build();
    }

    public static List<PetDTO> convertFromPetsToPetDTOs(List<Pet> pets) {
        if (pets == null) return new ArrayList<>();
        ArrayList<PetDTO> dtos = new ArrayList<>();
        for (Pet pet : pets) {
            dtos.add(convertFromPetToPetDTO(pet));
        }
        return dtos;
    }

    public static List<Pet> convertFromPetDTOsToPets(List<PetDTO> dtos) {
        if (dtos == null) return new ArrayList<>();
        ArrayList<Pet> pets = new ArrayList<>();
        for (PetDTO dto : dtos) {
            pets.add(convertFromPetDTOToPet(dto));
        }
        return pets;
    }

    public static Pet convertFromUpdatePetDTOToPet(UpdatePetDTO petDTO) {
        if (petDTO == null) return null;
        return Pet.builder()
                .id(petDTO.getId())
                .name(petDTO.getName())
                .gender(petDTO.getGender())
                .birthdate(petDTO.getBirthdate())
                .weight(petDTO.getWeight())
                .image(petDTO.getImage())
                .build();
    }
}
