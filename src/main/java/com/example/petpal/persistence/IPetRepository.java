package com.example.petpal.persistence;



import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;

import java.util.Optional;

public interface IPetRepository {
    Optional<Pet> getPet(Long petId);
    Pet updatePet(Long id, Pet pet);
    boolean deletePet(Long petId);
    Long createPet(Pet pet, User user);


}
