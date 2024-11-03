package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.persistence.entity.BreedEntity;

import java.util.ArrayList;
import java.util.List;

public class BreedConverter {
    private BreedConverter(){}

    public static BreedEntity convertFromBreedToBreedEntity(Breed breed){
        return BreedEntity.builder()
                .id(breed.getId())
                .name(breed.getName())
                .description(breed.getDescription())
                .normalMood(breed.getNormalMood() != null ?
                        MoodConverter.convertFromMoodToMoodEntity(breed.getNormalMood()) : null)
                .minimumExercisePerDay(breed.getMinimumExercisePerDay())
                .commonHealthProblems(breed.getCommonHealthProblems())
                .build();
    };



    public static Breed convertFromBreedEntityToBreed(BreedEntity entity){
        if (entity == null) return null;
        return Breed.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .normalMood(entity.getNormalMood() != null ?
                        MoodConverter.convertFromMoodEntityToMood(entity.getNormalMood()) : null)
                .minimumExercisePerDay(entity.getMinimumExercisePerDay())
                .commonHealthProblems(entity.getCommonHealthProblems())
                .build();
    };



    public static List<BreedEntity> convertFromBreedsToBreedEntities(List<Breed> breeds){
        List<BreedEntity> entities = new ArrayList<>();
        for (Breed b : breeds) {
            entities.add(convertFromBreedToBreedEntity(b));
        }
        return entities;
    };
    public static List<Breed> convertFromBreedEntitiesToBreeds(List<BreedEntity> entities){
        List<Breed> breeds = new ArrayList<>();
        for (BreedEntity entity : entities) {
            breeds.add(convertFromBreedEntityToBreed(entity));
        }
        return breeds;
    };


}
