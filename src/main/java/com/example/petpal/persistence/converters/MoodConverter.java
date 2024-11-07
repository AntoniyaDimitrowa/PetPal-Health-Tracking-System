package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.MoodEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MoodConverter {
    public static MoodEntity convertFromMoodToMoodEntity(Mood mood){
        if (mood == null) return null;
        return MoodEntity.builder()
                .id(mood.getId())
                .name(mood.getName())
                .emoji(mood.getEmoji())
                .build();
    }

    public static Mood convertFromMoodEntityToMood(MoodEntity entity){
        if (entity == null) return null;
        return Mood.builder()
                .id(entity.getId())
                .name(entity.getName())
                .emoji(entity.getEmoji())
                .build();
    }

    public static List<MoodEntity> convertFromMoodsToMoodEntities(List<Mood> moods){
        List<MoodEntity> entities = new ArrayList<>();
        for (Mood m : moods) {
            entities.add(convertFromMoodToMoodEntity(m));
        }
        return entities;
    }
    public static List<Mood> convertFromMoodEntitiesToMoods(List<MoodEntity> entities){
        List<Mood> moods = new ArrayList<>();
        for (MoodEntity entity : entities) {
            moods.add(convertFromMoodEntityToMood(entity));
        }
        return moods;
    }
}
