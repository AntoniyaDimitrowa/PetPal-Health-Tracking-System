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
    Pet createPet(String name, Breed breed, Gender gender, Date birthdate, double weight, ArrayList<VaccinationRecord> vaccinations) throws InvalidBreedException;
    void updatePet(long id, String name, Breed breed, Gender gender, Date birthdate, double weight) throws InvalidPetException, InvalidBreedException;
    boolean deletePet(long petId);
}
