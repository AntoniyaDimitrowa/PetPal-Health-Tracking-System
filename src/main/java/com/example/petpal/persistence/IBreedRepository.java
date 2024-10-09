package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IBreedRepository {
    ArrayList<BreedEntity> getAllBreeds();
    Optional<BreedEntity> getBreedById(long id);

    BreedEntity createBreed(BreedEntity breed);

    BreedEntity updateBreed(long id, BreedEntity updatedBreed);

    boolean deleteBreed(long id);

    Optional<BreedHealthInfoEntity> getHealthInfoForBreed(long breedId, int age);

    BreedEntity updateHealthProblems(long breedId, List<String> healthProblems);
}
