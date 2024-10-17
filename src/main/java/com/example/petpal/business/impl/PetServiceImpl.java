package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.persistence.converters.BreedConverter;
import com.example.petpal.persistence.converters.VaccinationConverter;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.PetEntity;
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
    public Optional<Pet> getPet(long petId) {
        return petRepository.getPet(petId).map(PetConverter::convertFromPetEntityToPet);
    }

    @Override
    public long createPet(Pet pet, long breedId, ArrayList<Long> vaccinationsIds) throws InvalidBreedException {
        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        ArrayList<VaccinationRecord> vaccinations = new ArrayList<>();

        for (long id : vaccinationsIds) {
            Vaccination v = VaccinationConverter.convertFromVaccinationEntitytoVaccination(vaccinationRepository.getVaccinationById(id).get());
            vaccinations.add(new VaccinationRecord(1L, v, new Date()));
        }
        pet.setBreed(breedOptional.get());
        pet.setVaccinationRecords(vaccinations);
        pet.setHealthRecords(new ArrayList<>());

        PetEntity newPet = PetConverter.convertFromPetToPetEntity(pet);

        long savedPetId = petRepository.createPet(newPet);
        return savedPetId;
    }

    @Override
    public void updatePet(Pet pet, long breedId) throws InvalidPetException, InvalidBreedException {
        Optional<PetEntity> petOptional = petRepository.getPet(pet.getId());
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(pet.getId());
        }
        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }
        PetEntity petEntity = PetConverter.convertFromPetToPetEntity(pet);
        petEntity.setBreed(BreedConverter.convertFromBreedToBreedEntity(breedOptional.get()));

        petRepository.updatePet(pet.getId(), petEntity);
    }

    @Override
    public boolean deletePet(long petId) {
        return this.petRepository.deletePet(petId);
    }


}
