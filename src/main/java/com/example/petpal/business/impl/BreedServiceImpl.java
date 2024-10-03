package com.example.petpal.business.impl;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.converters.HealthConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BreedServiceImpl implements IBreedService {
    private final IBreedRepository breedRepository;
    private final IMoodRepository moodRepository;

    public BreedServiceImpl(IBreedRepository breedRepository, IMoodRepository moodRepository) {
        this.breedRepository = breedRepository;
        this.moodRepository = moodRepository;
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
    public Breed createBreed(Breed breed) throws InvalidMoodException {
        Optional<MoodEntity> moodOptional = moodRepository.getMoodById(breed.getNormalMood().getId());
        if (moodOptional.isEmpty()) {
            throw new InvalidMoodException(breed.getNormalMood().getId());
        }
        return BreedConverter.convertFromBreedEntityToBreed(breedRepository.createBreed(BreedConverter.convertFromBreedToBreedEntity(breed)));
    }

    @Override
    public Breed updateBreed(long id, Breed updatedBreed) throws InvalidBreedException, InvalidMoodException {
        Optional<BreedEntity> breedOptional = breedRepository.getBreedById(id);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(id);
        }
        Optional<MoodEntity> moodOptional = moodRepository.getMoodById(updatedBreed.getNormalMood().getId());
        if (moodOptional.isEmpty()) {
            throw new InvalidMoodException(updatedBreed.getNormalMood().getId());
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
