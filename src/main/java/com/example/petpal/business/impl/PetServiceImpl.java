package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetServiceImpl implements IPetService {
    private final IPetRepository petRepository;
    private final IBreedRepository breedRepository;
    private final IVaccinationRepository vaccinationRepository;

    @Override
    public Optional<Pet> getPet(Long petId) {
        return petRepository.getPet(petId);
    }

    @Override
    public Long createPet(Pet pet, Long breedId, ArrayList<Long> vaccinationsIds) throws InvalidBreedException {
        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        ArrayList<VaccinationRecord> vaccinations = new ArrayList<>();
        for (Long id : vaccinationsIds) {
            Optional<Vaccination> vaccinationOptional = vaccinationRepository.getVaccinationById(id);
            vaccinationOptional.ifPresent(v -> vaccinations.add(new VaccinationRecord(null, v, new Date()))); // Set ID to null for new records
        }

        pet.setBreed(breedOptional.get());
        pet.setVaccinationRecords(vaccinations);
        pet.setHealthRecords(new ArrayList<>());

        return petRepository.createPet(pet);
    }

    @Override
    public void updatePet(Pet pet, Long breedId) throws InvalidPetException, InvalidBreedException {
        Optional<Pet> petOptional = petRepository.getPet(pet.getId());
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(pet.getId());
        }

        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        pet.setBreed(breedOptional.get());

        petRepository.updatePet(pet.getId(), pet);
    }

    @Override
    public boolean deletePet(Long petId) {
        return petRepository.deletePet(petId);
    }
}
