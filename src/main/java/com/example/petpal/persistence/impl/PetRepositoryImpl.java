package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IPetRepositoryJPA;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.persistence.converters.UserConverter;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public Pet updatePet(Long id, Pet pet) {
        Optional<PetEntity> existingPetOptional = petRepositoryJPA.findById(id);

        if(!existingPetOptional.isEmpty()) {
            PetEntity existingPet = existingPetOptional.get();
            PetEntity updatedPet = PetConverter.convertFromPetToPetEntity(pet);
            // Update the fields of the existing entity
            existingPet.setName(pet.getName());
            existingPet.setBreed(updatedPet.getBreed());
            existingPet.setBirthdate(pet.getBirthdate());
            existingPet.setWeight(pet.getWeight());
            existingPet.setGender(pet.getGender());
            existingPet.setImage(pet.getImage());

            // Save the updated entity
            petRepositoryJPA.save(existingPet);
        }
        return null;
    }


    @Override
    @Transactional
    public boolean deletePet(Long petId) {
        if (petRepositoryJPA.existsById(petId)) {
            try {
                petRepositoryJPA.deletePetById(petId);
                return true;
            } catch (Exception e) {
                return false;
            }
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
