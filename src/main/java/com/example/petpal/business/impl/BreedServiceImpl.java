package com.example.petpal.business.impl;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BreedServiceImpl implements IBreedService {
    private final IBreedRepository breedRepository;
    private final IMoodRepository moodRepository;

    @Override
    public List<Breed> getAllBreeds() {
        return new ArrayList<>(breedRepository.getAllBreeds());
    }

    @Override
    public Optional<Breed> getBreedById(Long id) {
        return breedRepository.getBreedById(id);
    }

    @Override
    public Long createBreed(Breed breed, Long normalMoodId) throws InvalidMoodException {
        Mood mood = moodRepository.getMoodById(normalMoodId)
                .orElseThrow(() -> new InvalidMoodException(normalMoodId));

        breed.setNormalMood(mood);
        return breedRepository.createBreed(breed);
    }

    @Override
    public Breed updateBreed(Long id, Breed updatedBreed, Long normalMoodId) throws InvalidBreedException, InvalidMoodException {
        Breed existingBreed = breedRepository.getBreedById(id)
                .orElseThrow(() -> new InvalidBreedException(updatedBreed.getId()));

        Mood mood = moodRepository.getMoodById(normalMoodId)
                .orElseThrow(() -> new InvalidMoodException(normalMoodId));

        updatedBreed.setNormalMood(mood);
        return breedRepository.updateBreed(existingBreed.getId(), updatedBreed);
    }

    @Override
    public boolean deleteBreed(Long id) {
        return breedRepository.deleteBreed(id);
    }

    @Override
    public Breed updateHealthProblems(Long breedId, List<String> healthProblems) throws InvalidBreedException {
        if(breedRepository.getBreedById(breedId).isEmpty()) {
            throw new InvalidBreedException(breedId);
        }

        return breedRepository.updateHealthProblems(breedId, healthProblems);
    }
}
