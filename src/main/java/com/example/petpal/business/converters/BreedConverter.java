package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;

import java.util.ArrayList;
import java.util.List;

public class BreedConverter {
    private BreedConverter(){}

    public static BreedEntity convertFromBreedToBreedEntity(Breed breed){
        return BreedEntity.builder()
                .id(breed.getId())
                .name(breed.getName())
                .description(breed.getDescription())
                .normalMood(convertFromMoodToMoodEntity(breed.getNormalMood()))
                .minimumExercisePerDay(breed.getMinimumExercisePerDay())
                .commonHealthProblems(breed.getCommonHealthProblems())
                .build();
    };

    public static MoodEntity convertFromMoodToMoodEntity(Mood mood){
        return MoodEntity.builder()
                .id(mood.getId())
                .name(mood.getName())
                .image(mood.getImage())
                .build();
    };

    public static Breed convertFromBreedEntityToBreed(BreedEntity entity){
        return Breed.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .normalMood(convertFromMoodEntityToMood(entity.getNormalMood()))
                .minimumExercisePerDay(entity.getMinimumExercisePerDay())
                .commonHealthProblems(entity.getCommonHealthProblems())
                .build();
    };

    public static Mood convertFromMoodEntityToMood(MoodEntity entity){
        return Mood.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
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

    public static ArrayList<MoodEntity> convertFromMoodsToMoodEntities(ArrayList<Mood> moods){
        ArrayList<MoodEntity> entities = new ArrayList<>();
        for (Mood m : moods) {
            entities.add(convertFromMoodToMoodEntity(m));
        }
        return entities;
    };
    public static ArrayList<Mood> convertFromMoodEntitiesToMoods(ArrayList<MoodEntity> entities){
        ArrayList<Mood> moods = new ArrayList<>();
        for (MoodEntity entity : entities) {
            moods.add(convertFromMoodEntityToMood(entity));
        }
        return moods;
    };
}
