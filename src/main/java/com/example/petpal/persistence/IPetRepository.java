package com.example.petpal.persistence;



import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.Optional;

public interface IPetRepository {
    Optional<Pet> getPet(Long petId);
    void updatePet(Long id, Pet pet);
    boolean deletePet(Long petId);
    Long createPet(Pet pet, User user);


}
