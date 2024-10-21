package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.MoodEntity;

import java.util.ArrayList;

public class MoodConverter {
    private MoodConverter(){}
    public static MoodEntity convertFromMoodToMoodEntity(Mood mood){
        if (mood == null) return null;
        return MoodEntity.builder()
                .id(mood.getId())
                .name(mood.getName())
                .emoji(mood.getEmoji())
                .build();
    };

    public static Mood convertFromMoodEntityToMood(MoodEntity entity){
        if (entity == null) return null;
        return Mood.builder()
                .id(entity.getId())
                .name(entity.getName())
                .emoji(entity.getEmoji())
                .build();
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
