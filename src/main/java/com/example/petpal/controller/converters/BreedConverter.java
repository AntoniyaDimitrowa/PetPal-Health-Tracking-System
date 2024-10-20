package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.controller.dto.breed.BreedDTO;
import com.example.petpal.controller.dto.breed.CreateBreedDTO;
import com.example.petpal.controller.dto.breed.UpdateBreedDTO;

import java.util.ArrayList;

public class BreedConverter {
    private BreedConverter() {}

    public static BreedDTO convertFromBreedToBreedDTO(Breed breed) {
        if (breed == null) return null;
        return BreedDTO.builder()
                .id(breed.getId())
                .name(breed.getName())
                .description(breed.getDescription())
                .normalMood(MoodConverter.convertFromMoodToMoodDTO(breed.getNormalMood()))
                .minimumExercisePerDay(breed.getMinimumExercisePerDay())
                .commonHealthProblems(breed.getCommonHealthProblems())
                .build();
    }

    public static Breed convertFromBreedDTOToBreed(BreedDTO dto) {
        if (dto == null) return null;
        return Breed.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .normalMood(MoodConverter.convertFromMoodDTOToMood(dto.getNormalMood()))
                .minimumExercisePerDay(dto.getMinimumExercisePerDay())
                .commonHealthProblems(dto.getCommonHealthProblems())
                .build();
    }

    public static ArrayList<BreedDTO> convertFromBreedsToBreedDTOs(ArrayList<Breed> breeds) {
        if (breeds == null) return new ArrayList<>();
        ArrayList<BreedDTO> dtos = new ArrayList<>();
        for (Breed b : breeds) {
            dtos.add(convertFromBreedToBreedDTO(b));
        }
        return dtos;
    }

    public static Breed convertFromCreateBreedDTOToBreed(CreateBreedDTO dto) {
        return Breed.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .minimumExercisePerDay(dto.getMinimumExercisePerDay())
                .commonHealthProblems(dto.getCommonHealthProblems())
                .build();
    }

    public static Breed convertFromUpdateBreedDTOToBreed(UpdateBreedDTO dto) {
        return Breed.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .minimumExercisePerDay(dto.getMinimumExercisePerDay())
                .commonHealthProblems(dto.getCommonHealthProblems())
                .build();
    }
}
