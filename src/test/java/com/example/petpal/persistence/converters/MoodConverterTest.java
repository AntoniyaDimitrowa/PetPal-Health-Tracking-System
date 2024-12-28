package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.MoodEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class MoodConverterTest {

    private static final Mood moodHappy = Mood.builder().id(1L).name("Happy").emoji("😊").build();
    private static final Mood moodSad = Mood.builder().id(2L).name("Sad").emoji("😢").build();

    private static final MoodEntity moodEntityHappy = MoodEntity.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();
    private static final MoodEntity moodEntitySad = MoodEntity.builder()
            .id(2L)
            .name("Sad")
            .emoji("😢")
            .build();

    @Test
    void convertFromMoodToMoodEntity_shouldConvertSuccessfully() {
        MoodEntity result = MoodConverter.convertFromMoodToMoodEntity(moodHappy);

        assertNotNull(result);
        assertEquals(moodHappy.getId(), result.getId());
        assertEquals(moodHappy.getName(), result.getName());
        assertEquals(moodHappy.getEmoji(), result.getEmoji());
    }

    @Test
    void convertFromMoodToMoodEntity_shouldReturnNullForNullMood() {
        assertNull(MoodConverter.convertFromMoodToMoodEntity(null));
    }

    @Test
    void convertFromMoodEntityToMood_shouldConvertSuccessfully() {
        Mood result = MoodConverter.convertFromMoodEntityToMood(moodEntityHappy);

        assertNotNull(result);
        assertEquals(moodEntityHappy.getId(), result.getId());
        assertEquals(moodEntityHappy.getName(), result.getName());
        assertEquals(moodEntityHappy.getEmoji(), result.getEmoji());
    }

    @Test
    void convertFromMoodEntityToMood_shouldReturnNullForNullMoodEntity() {
        assertNull(MoodConverter.convertFromMoodEntityToMood(null));
    }

    @Test
    void convertFromMoodsToMoodEntities_shouldConvertListSuccessfully() {
        List<Mood> moods = List.of(moodHappy, moodSad);
        List<MoodEntity> result = MoodConverter.convertFromMoodsToMoodEntities(moods);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(moodHappy.getId(), result.get(0).getId());
        assertEquals(moodSad.getId(), result.get(1).getId());
    }

    @Test
    void convertFromMoodEntitiesToMoods_shouldConvertListSuccessfully() {
        List<MoodEntity> entities = List.of(moodEntityHappy, moodEntitySad);
        List<Mood> result = MoodConverter.convertFromMoodEntitiesToMoods(entities);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(moodEntityHappy.getId(), result.get(0).getId());
        assertEquals(moodEntitySad.getId(), result.get(1).getId());
    }
}
