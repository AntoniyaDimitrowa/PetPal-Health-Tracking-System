package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.VaccinationEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepositoryImpl implements IPetRepository {
    private final ArrayList<PetEntity> pets = new ArrayList<>();
    private static long nextId = 1;

    @Override
    public Optional<PetEntity> getPet(long id) {
        return pets.stream().filter(pet -> pet.getId() == id).findFirst();
    }

    @Override
    public void updatePet(long id, String name, BreedEntity breed, Gender gender, Date birthdate, Double weight) {

    }

    @Override
    public void deletePet(long petId) {
        pets.removeIf(pet -> pet.getId() == petId);
    }

    @Override
    public PetEntity createPet(PetEntity pet) {
        if (pet.getId() == 0) {
            pet.setId(nextId++);
            pets.add(pet);
        } else {
            pets.removeIf(p -> p.getId() == pet.getId());
            pets.add(pet);
        }
        return pet;
    }
}
