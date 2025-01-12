package com.example.petpal.business;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.exception.*;

import java.util.List;
import java.util.Optional;

public interface IPetService {
    Optional<Pet> getPet(Long petId) throws UnauthorizedDataAccessException;
    Long createPet(Pet pet, Long breedId, List<Long> vaccinationsIds, Long userId) throws InvalidBreedException, InvalidVaccinationException, InvalidUserException, CreationFailException, UnauthorizedDataAccessException;
    void updatePet(Pet pet, Long breedId) throws InvalidPetException, InvalidBreedException, UnauthorizedDataAccessException;
    boolean deletePet(Long petId) throws UnauthorizedDataAccessException;
}
