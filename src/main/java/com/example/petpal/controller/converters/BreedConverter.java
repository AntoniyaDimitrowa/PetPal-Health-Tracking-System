package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.BreedDTO;
import com.example.petpal.controller.dto.MoodDTO;

public class BreedConverter {
    private BreedConverter(){}

    public static BreedDTO convertFromBreedToBreedDTO(Breed breed){
        return BreedDTO.builder()
                .name(breed.getName())
                .description(breed.getDescription())
                .normalMood(convertFromMoodToMoodDTO(breed.getNormalMood()))
                .minimumExercisePerDay(breed.getMinimumExercisePerDay())
                .commonHealthProblems(breed.getCommonHealthProblems())
                .build();
    };

    public static MoodDTO convertFromMoodToMoodDTO(Mood mood){
        return MoodDTO.builder()
                .name(mood.getName())
                .build();
    };

    public static Breed convertFromBreedDTOToBreed(BreedDTO dto){
        return Breed.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .normalMood(convertFromMoodDTOToMood(dto.getNormalMood()))
                .minimumExercisePerDay(dto.getMinimumExercisePerDay())
                .commonHealthProblems(dto.getCommonHealthProblems())
                .build();
    };

    public static Mood convertFromMoodDTOToMood(MoodDTO dto){
        return Mood.builder()
                .name(dto.getName())
                .build();
    };
}
