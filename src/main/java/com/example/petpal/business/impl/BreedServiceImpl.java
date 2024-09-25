package com.example.petpal.business.impl;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.converters.HealthConverter;
import com.example.petpal.business.converters.PetConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BreedServiceImpl implements IBreedService {
    private final IBreedRepository breedRepository;

    public BreedServiceImpl(IBreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @Override
    public ArrayList<Breed> getAllBreeds() {
        return BreedConverter.convertFromBreedEntitiesToBreeds(breedRepository.getAllBreeds());
    }

    @Override
    public Optional<Breed> getBreedById(long id) {
        return breedRepository.getBreedById(id).map(BreedConverter::convertFromBreedEntityToBreed);
    }

    @Override
    public Breed addBreed(Breed breed) {
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.addBreed(BreedConverter.convertFromBreedToBreedEntity(breed)));
    }

    @Override
    public Breed updateBreed(long id, Breed updatedBreed) throws InvalidBreedException {
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(id);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(id);
        }
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.updateBreed(id, BreedConverter.convertFromBreedToBreedEntity(updatedBreed)));
    }

    @Override
    public boolean deleteBreed(long id) {
        return breedRepository.deleteBreed(id);
    }

    @Override
    public Optional<BreedHealthInfo> getHealthInfoForBreed(long breedId, int age) throws InvalidBreedException {
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }
        return breedRepository.getHealthInfoForBreed(breedId, age).map(HealthConverter::convertFromBreedHealthInfoEntityToBreedHealthInfo);
    }


    @Override
    public Breed updateHealthProblems(long breedId, ArrayList<String> healthProblems) throws InvalidBreedException {
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.updateHealthProblems(breedId, healthProblems));
    }
}
