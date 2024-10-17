package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface IBreedService {
    ArrayList<Breed> getAllBreeds();

    Optional<Breed> getBreedById(long id);

    long createBreed(Breed breed, long normalMoodId) throws InvalidMoodException;

    Breed updateBreed(Breed updatedBreed, long normalMoodId) throws InvalidBreedException, InvalidMoodException;

    boolean deleteBreed(long id);

    Optional<BreedHealthInfo> getHealthInfoForBreed(long breedId, int age) throws InvalidBreedException;

    Breed updateHealthProblems(long breedId, ArrayList<String> healthProblems) throws InvalidBreedException;
}
