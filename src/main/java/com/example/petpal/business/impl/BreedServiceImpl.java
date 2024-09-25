package com.example.petpal.business.impl;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.converters.HealthConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.Optional;

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
        return Optional.ofNullable(BreedConverter.convertFromBreedEntityToBreed(breedRepository.getBreedById(id).get()));
    }

    @Override
    public Breed addBreed(Breed breed) {
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.addBreed(BreedConverter.convertFromBreedToBreedEntity(breed)));
    }

    @Override
    public Breed updateBreed(long id, Breed updatedBreed) {
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.updateBreed(id, BreedConverter.convertFromBreedToBreedEntity(updatedBreed)));
    }

    @Override
    public boolean deleteBreed(long id) {
        return breedRepository.deleteBreed(id);
    }

    @Override
    public Optional<BreedHealthInfo> getHealthInfoForBreed(long breedId, int age) {
        return Optional.ofNullable(HealthConverter.convertFromBreedHealthInfoEntityToBreedHealthInfo(breedRepository.getHealthInfoForBreed(breedId, age).get()));
    }

    @Override
    public ArrayList<Mood> getMoodsForBreed(long breedId) {
        return BreedConverter.convertFromMoodEntitiesToMoods(breedRepository.getMoodsForBreed(breedId));
    }

    @Override
    public Breed updateHealthProblems(long breedId, ArrayList<String> healthProblems) {
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.updateHealthProblems(breedId, healthProblems));
    }
}
