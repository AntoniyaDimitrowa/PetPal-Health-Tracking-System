package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.persistence.entity.BreedEntity;

import java.util.ArrayList;

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



    public static ArrayList<BreedEntity> convertFromBreedsToBreedEntities(ArrayList<Breed> breeds){
        ArrayList<BreedEntity> entities = new ArrayList<>();
        for (Breed b : breeds) {
            entities.add(convertFromBreedToBreedEntity(b));
        }
        return entities;
    };
    public static ArrayList<Breed> convertFromBreedEntitiesToBreeds(ArrayList<BreedEntity> entities){
        ArrayList<Breed> breeds = new ArrayList<>();
        for (BreedEntity entity : entities) {
            breeds.add(convertFromBreedEntityToBreed(entity));
        }
        return breeds;
    };


}
