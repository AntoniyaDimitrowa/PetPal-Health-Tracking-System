package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public interface IPetService {
    Optional<Pet> getPet(long petId);
    long createPet(Pet pet, long breedId, ArrayList<Long> vaccinationsIds) throws InvalidBreedException;
    void updatePet(Pet pet, long breedId) throws InvalidPetException, InvalidBreedException;
    boolean deletePet(long petId);
}
