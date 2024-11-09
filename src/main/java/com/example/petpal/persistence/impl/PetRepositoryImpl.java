package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IPetRepositoryJPA;
import com.example.petpal.persistence.IUserRepositoryJPA;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PetRepositoryImpl implements IPetRepository {
    private final IPetRepositoryJPA petRepositoryJPA;
    private final IUserRepositoryJPA userRepositoryJPA;

    public PetRepositoryImpl(IPetRepositoryJPA petRepositoryJPA, IUserRepositoryJPA userRepositoryJPA) {
        this.petRepositoryJPA = petRepositoryJPA;
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public Optional<Pet> getPet(Long petId) {
        return petRepositoryJPA.findById(petId)
                .map(PetConverter::convertFromPetEntityToPet);
    }

    @Override
    public void updatePet(Long id, Pet pet) {
        PetEntity petEntity = PetConverter.convertFromPetToPetEntity(pet);
        petEntity.setId(id);
        petRepositoryJPA.save(petEntity);
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
