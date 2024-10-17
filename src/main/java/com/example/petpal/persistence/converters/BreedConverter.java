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
                .normalMood(MoodConverter.convertFromMoodToMoodEntity(breed.getNormalMood()))
                .minimumExercisePerDay(breed.getMinimumExercisePerDay())
                .commonHealthProblems(breed.getCommonHealthProblems())
                .build();
    };



    public static Breed convertFromBreedEntityToBreed(BreedEntity entity){
        return Breed.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .normalMood(MoodConverter.convertFromMoodEntityToMood(entity.getNormalMood()))
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
