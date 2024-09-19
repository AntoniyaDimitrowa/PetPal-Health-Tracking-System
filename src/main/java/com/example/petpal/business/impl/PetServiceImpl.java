package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class PetServiceImpl implements IPetService {
    private final IPetRepository petRepository;

    public PetServiceImpl(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Optional<Pet> getPet(long petId) {
//        Optional<PetEntity> petOptional = petRepository.getPet(petId);
//        if (petOptional.isEmpty()) {
//            throw new InvalidPetException(petId);
//        }
        return Optional.empty();
    }

    @Override
    public void updatePet(long id, String name, Breed breed, Gender gender, Date birthdate, Double weight) throws InvalidPetException {

    }

    @Override
    public void deletePet(long petId) {

    }

    @Override
    public Pet createPet(String name, Breed breed, Gender gender, Date birthdate, Double weight, ArrayList<VaccinationRecord> vaccinations) {
        return null;
    }
}
