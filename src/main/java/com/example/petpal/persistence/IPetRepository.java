package com.example.petpal.persistence;



import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.VaccinationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public interface IPetRepository {
    Optional<PetEntity> getPet(long petId);
    void updatePet(long id, String name, BreedEntity breed, Gender gender, Date birthdate, Double weight);
    void deletePet(long petId);
    PetEntity createPet(PetEntity pet);
}
