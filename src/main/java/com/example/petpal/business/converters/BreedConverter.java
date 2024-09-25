package com.example.petpal.business.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;

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
}
