package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IPetRepositoryJPA;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PetRepositoryImpl implements IPetRepository {
    private final IPetRepositoryJPA petRepositoryJPA;

    public PetRepositoryImpl(IPetRepositoryJPA petRepositoryJPA) {
        this.petRepositoryJPA = petRepositoryJPA;
    }

    @Override
    public Optional<Pet> getPet(Long petId) {
        return petRepositoryJPA.findById(petId)
                .map(PetConverter::convertFromPetEntityToPet);
    }

    @Override
    public void updatePet(Long id, Pet pet) {
        PetEntity existingPet = petRepositoryJPA.findById(id).get();

        PetEntity updatedPet = PetConverter.convertFromPetToPetEntity(pet);
        updatedPet.setId(id);
        updatedPet.setOwner(existingPet.getOwner());
        petRepositoryJPA.save(updatedPet);
    }

    @Override
    public boolean deletePet(Long petId) {
        if (petRepositoryJPA.existsById(petId)) {
            petRepositoryJPA.deleteById(petId);
            return true;
        }
        return false;
    }

    @Override
    public Long createPet(Pet pet, User user) {
        PetEntity petEntity = PetConverter.convertFromPetToPetEntity(pet);
        petEntity.setOwner(UserConverter.convertFromUserToUserEntity(user));
        return petRepositoryJPA.save(petEntity).getId();
    }
}
