package com.example.petpal.business.impl;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.converters.BreedConverter;
import com.example.petpal.persistence.converters.HealthConverter;
import com.example.petpal.persistence.converters.MoodConverter;
import com.example.petpal.persistence.entity.MoodEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BreedServiceImpl implements IBreedService {
    private final IBreedRepository breedRepository;
    private final IMoodRepository moodRepository;

    @Override
    public ArrayList<Breed> getAllBreeds() {
        return new ArrayList<>(breedRepository.getAllBreeds());
    }

    @Override
    public Optional<Breed> getBreedById(Long id) {
        return breedRepository.getBreedById(id);
    }

    @Override
    public Long createBreed(Breed breed, Long normalMoodId) throws InvalidMoodException {
        Optional<Mood> moodOptional = moodRepository.getMoodById(normalMoodId);
        if (moodOptional.isEmpty()) {
            throw new InvalidMoodException(normalMoodId);
        }
        breed.setNormalMood(moodOptional.get());
        return breedRepository.createBreed(breed);
    }

    @Override
    public Breed updateBreed(Breed updatedBreed, Long normalMoodId) throws InvalidBreedException, InvalidMoodException {
        Optional<Breed> existingBreedOpt = breedRepository.getBreedById(updatedBreed.getId());
        if (existingBreedOpt.isEmpty()) {
            throw new InvalidBreedException(updatedBreed.getId());
        }
        Optional<Mood> moodOptional = moodRepository.getMoodById(normalMoodId);
        if (moodOptional.isEmpty()) {
            throw new InvalidMoodException(normalMoodId);
        }
        updatedBreed.setNormalMood(moodOptional.get());
        return breedRepository.updateBreed(updatedBreed.getId(), updatedBreed);
    }

    @Override
    public boolean deleteBreed(Long id) {
        return breedRepository.deleteBreed(id);
    }

    @Override
    public Optional<BreedHealthInfo> getHealthInfoForBreed(Long breedId, int age) throws InvalidBreedException {
        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }
        return breedRepository.getHealthInfoForBreed(breedId, age);
    }

    @Override
    public Breed updateHealthProblems(Long breedId, ArrayList<String> healthProblems) throws InvalidBreedException {
        Optional<Breed> breedOptional = breedRepository.getBreedById(breedId);
        if (breedOptional.isEmpty()) {
            throw new InvalidBreedException(breedId);
        }
        return breedRepository.updateHealthProblems(breedId, healthProblems);
    }
}
