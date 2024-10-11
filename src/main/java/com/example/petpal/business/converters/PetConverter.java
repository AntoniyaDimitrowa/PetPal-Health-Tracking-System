package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.PetEntity;

import java.util.ArrayList;

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
                .image(ImageConverter.encodeToBase64(pet.getImage()))
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
                .image(ImageConverter.decodeFromBase64(pet.getImage()))
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(pet.getVaccinationRecords()))
                .healthRecords(HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(pet.getHealthRecords()))
                .build();
    };

    public static ArrayList<PetEntity> convertFromPetsToPetEntities(ArrayList<Pet> pets){
        ArrayList<PetEntity> entities = new ArrayList<>();
        for (Pet p : pets) {
            entities.add(convertFromPetToPetEntity(p));
        }
        return entities;
    };

    public static ArrayList<Pet> convertFromPetEntitiesToPets(ArrayList<PetEntity> entities){
        ArrayList<Pet> pets = new ArrayList<>();
        for (PetEntity entity : entities) {
            pets.add(convertFromPetEntityToPet(entity));
        }
        return pets;
    };
}
