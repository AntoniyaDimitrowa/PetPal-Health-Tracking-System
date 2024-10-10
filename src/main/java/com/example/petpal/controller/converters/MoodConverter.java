package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.mood.MoodDTO;

import java.util.ArrayList;

public class MoodConverter {
    private MoodConverter(){}
    public static MoodDTO convertFromMoodToMoodDTO(Mood mood){
        return MoodDTO.builder()
                .name(mood.getName())
                .image(mood.getImage())
                .build();
    };

    public static Mood convertFromMoodDTOToMood(MoodDTO dto){
        return Mood.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .build();
    };

    public static ArrayList<MoodDTO> convertFromMoodsToMoodDTOs(ArrayList<Mood> moods){
        ArrayList<MoodDTO> dtos = new ArrayList<>();
        for (Mood m : moods) {
            dtos.add(convertFromMoodToMoodDTO(m));
        }
        return dtos;
    };
    public static ArrayList<Mood> convertFromMoodDTOsToMoods(ArrayList<MoodDTO> dtos){
        ArrayList<Mood> moods = new ArrayList<>();
        for (MoodDTO dto : dtos) {
            moods.add(convertFromMoodDTOToMood(dto));
        }
        return moods;
    };
}
