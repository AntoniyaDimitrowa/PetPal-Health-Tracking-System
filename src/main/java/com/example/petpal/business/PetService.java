package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public interface PetService {
    Optional<Pet> getPet(long petId);
    void updatePet(String name, Breed breed, Gender gender, Date birthdate, Double weight) throws InvalidPetException;
    void deletePet(long petId);
    Pet createPet(String name, Breed breed, Gender gender, Date birthdate, Double weight, ArrayList<Vaccination> vaccinations); //throws InvalidCountryException, PcnAlreadyExistsException;
}
