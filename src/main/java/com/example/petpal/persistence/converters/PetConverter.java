package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.persistence.entity.PetEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetConverter {
    public static PetEntity convertFromPetToPetEntity(Pet pet){
        if (pet == null) return null;
        return PetEntity.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed() != null ? BreedConverter.convertFromBreedToBreedEntity(pet.getBreed()) : null)
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .vaccinationRecords(pet.getVaccinationRecords() != null ?
                        VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsEntities(pet.getVaccinationRecords()) : new ArrayList<>())
                .healthRecords(pet.getHealthRecords() != null ?
                        HealthConverter.convertFromHealthRecordsToHealthRecordEntities(pet.getHealthRecords()) : new ArrayList<>())
                .build();
    }

    public static Pet convertFromPetEntityToPet(PetEntity pet){
        if (pet == null) return null;
        return Pet.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed() != null ? BreedConverter.convertFromBreedEntityToBreed(pet.getBreed()) : null)
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .weight(pet.getWeight())
                .image(pet.getImage())
                .vaccinationRecords(pet.getVaccinationRecords() != null ?
                        VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(pet.getVaccinationRecords()) : new ArrayList<>())
                .healthRecords(pet.getHealthRecords() != null ?
                        HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(pet.getHealthRecords()) : new ArrayList<>())
                .build();
    }

    public static List<PetEntity> convertFromPetsToPetEntities(List<Pet> pets){
        List<PetEntity> entities = new ArrayList<>();
        for (Pet p : pets) {
            entities.add(convertFromPetToPetEntity(p));
        }
        return entities;
    }

    public static List<Pet> convertFromPetEntitiesToPets(List<PetEntity> entities){
        List<Pet> pets = new ArrayList<>();
        for (PetEntity entity : entities) {
            pets.add(convertFromPetEntityToPet(entity));
        }
        return pets;
    }
}
