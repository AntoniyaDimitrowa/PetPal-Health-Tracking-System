package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface IBreedService {
    ArrayList<Breed> getAllBreeds();

    Optional<Breed> getBreedById(long id);

    Breed addBreed(Breed breed);

    Breed updateBreed(long id, Breed updatedBreed);

    boolean deleteBreed(long id);

    Optional<BreedHealthInfo> getHealthInfoForBreed(long breedId, int age);

    ArrayList<Mood> getMoodsForBreed(long breedId);

    Breed updateHealthProblems(long breedId, ArrayList<String> healthProblems);
}
