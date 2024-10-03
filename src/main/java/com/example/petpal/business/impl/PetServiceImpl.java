package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.converters.VaccinationConverter;
import com.example.petpal.business.converters.PetConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class PetServiceImpl implements IPetService {
    private final IPetRepository petRepository;
    private final IBreedRepository breedRepository;

    public PetServiceImpl(IPetRepository petRepository, IBreedRepository breedRepository) {
        this.petRepository = petRepository;
        this.breedRepository = breedRepository;
    }

    @Override
    public Optional<Pet> getPet(long petId) {
        return petRepository.getPet(petId).map(PetConverter::convertFromPetEntityToPet);
    }

    @Override
    public Pet createPet(String name, Breed breed, Gender gender, Date birthdate, double weight, ArrayList<VaccinationRecord> vaccinations) throws InvalidBreedException {
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(breed.getId());
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breed.getId());
        }
        PetEntity newPet = PetEntity.builder()
                .name(name)
                .breed(BreedConverter.convertFromBreedToBreedEntity(breed))
                .gender(gender)
                .birthdate(birthdate)
                .weight(weight)
                .vaccinationRecords(VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsEntities(vaccinations))
                .healthRecords(new ArrayList<>())
                .build();

        PetEntity savedPet = petRepository.createPet(newPet);
        return PetConverter.convertFromPetEntityToPet(savedPet);
    }

    @Override
    public void updatePet(long id, String name, Breed breed, Gender gender, Date birthdate, double weight) throws InvalidPetException, InvalidBreedException {
        Optional<PetEntity> petOptional = petRepository.getPet(id);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(id);
        }
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(breed.getId());
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breed.getId());
        }
        PetEntity petEntity = petOptional.get();

        petRepository.updatePet(id, name, BreedConverter.convertFromBreedToBreedEntity(breed), gender, birthdate, weight);
    }

    @Override
    public boolean deletePet(long petId) {
        return this.petRepository.deletePet(petId);
    }


}
