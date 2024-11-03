package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IBreedService {
    List<Breed> getAllBreeds();

    Optional<Breed> getBreedById(Long id);

    Long createBreed(Breed breed, Long normalMoodId) throws InvalidMoodException;

    Breed updateBreed(Breed updatedBreed, Long normalMoodId) throws InvalidBreedException, InvalidMoodException;

    boolean deleteBreed(Long id);

    Breed updateHealthProblems(Long breedId, List<String> healthProblems) throws InvalidBreedException;
}
