package com.example.petpal.persistence;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IBreedRepository {
    ArrayList<Breed> getAllBreeds();
    Optional<Breed> getBreedById(Long id);

    Long createBreed(Breed breed);

    boolean deleteBreed(Long id);

    Breed updateBreed(Long id, Breed updatedBreed);

    Breed updateHealthProblems(Long breedId, ArrayList<String> healthProblems);
}
