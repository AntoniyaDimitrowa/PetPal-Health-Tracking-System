package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.mood.MoodDTO;

import java.util.ArrayList;
import java.util.List;

public class MoodConverter {
    private MoodConverter() {}

    public static MoodDTO convertFromMoodToMoodDTO(Mood mood) {
        if (mood == null) return null;
        return MoodDTO.builder()
                .id(mood.getId())
                .name(mood.getName())
                .emoji(mood.getEmoji())
                .build();
    }

    public static Mood convertFromMoodDTOToMood(MoodDTO dto) {
        if (dto == null) return null;
        return Mood.builder()
                .id(dto.getId())
                .name(dto.getName())
                .emoji(dto.getEmoji())
                .build();
    }

    public static List<MoodDTO> convertFromMoodsToMoodDTOs(List<Mood> moods) {
        if (moods == null) return new ArrayList<>();
        ArrayList<MoodDTO> dtos = new ArrayList<>();
        for (Mood m : moods) {
            dtos.add(convertFromMoodToMoodDTO(m));
        }
        return dtos;
    }

    public static List<Mood> convertFromMoodDTOsToMoods(List<MoodDTO> dtos) {
        if (dtos == null) return new ArrayList<>();
        ArrayList<Mood> moods = new ArrayList<>();
        for (MoodDTO dto : dtos) {
            moods.add(convertFromMoodDTOToMood(dto));
        }
        return moods;
    }
}
