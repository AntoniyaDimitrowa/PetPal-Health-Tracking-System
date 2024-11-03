package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public Long createPet(Pet pet, Long breedId, List<Long> vaccinationsIds) throws InvalidBreedException, InvalidVaccinationException {
        Breed breed = breedRepository.getBreedById(breedId)
                .orElseThrow(() -> new InvalidBreedException(breedId));

        List<VaccinationRecord> vaccinations = new ArrayList<>();
        for (Long id : vaccinationsIds) {
            Vaccination vaccination = vaccinationRepository.getVaccinationById(id)
                    .orElseThrow(() -> new InvalidVaccinationException(id));
            vaccinations.add(new VaccinationRecord(null, vaccination, new Date()));
        }

        pet.setBreed(breed);
        pet.setVaccinationRecords(vaccinations);
        pet.setHealthRecords(new ArrayList<>());

        return petRepository.createPet(pet);
    }

    @Override
    public void updatePet(Pet pet, Long breedId) throws InvalidPetException, InvalidBreedException {
        Pet existingPet = petRepository.getPet(pet.getId())
                .orElseThrow(() -> new InvalidPetException(pet.getId()));

        Breed breed = breedRepository.getBreedById(breedId)
                .orElseThrow(() -> new InvalidBreedException(breedId));

        pet.setBreed(breed);

        petRepository.updatePet(existingPet.getId(), pet);
    }

    @Override
    public boolean deletePet(Long petId) {
        return petRepository.deletePet(petId);
    }
}
