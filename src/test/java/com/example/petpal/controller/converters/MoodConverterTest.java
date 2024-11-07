package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.mood.MoodDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoodConverterTest {

    private static final Mood moodHappy = Mood.builder().id(1L).name("Happy").emoji("😊").build();
    private static final Mood moodSad = Mood.builder().id(2L).name("Sad").emoji("😢").build();

    private static final MoodDTO moodDTOHappy = MoodDTO.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();

    private static final MoodDTO moodDTOSad = MoodDTO.builder()
            .id(2L)
            .name("Sad")
            .emoji("😢")
            .build();

    @Test
    void convertFromMoodToMoodDTO_shouldReturnDTOWhenMoodIsValid() {
        MoodDTO dto = MoodConverter.convertFromMoodToMoodDTO(moodHappy);

        assertNotNull(dto);
        assertEquals(moodHappy.getId(), dto.getId());
        assertEquals(moodHappy.getName(), dto.getName());
        assertEquals(moodHappy.getEmoji(), dto.getEmoji());
    }

    @Test
    void convertFromMoodToMoodDTO_shouldReturnNullForNullMood() {
        assertNull(MoodConverter.convertFromMoodToMoodDTO(null));
    }

    @Test
    void convertFromMoodsToMoodDTOs_shouldConvertListSuccessfully() {
        List<Mood> moods = List.of(moodHappy, moodSad);
        List<MoodDTO> result = MoodConverter.convertFromMoodsToMoodDTOs(moods);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(moodHappy.getId(), result.get(0).getId());
        assertEquals(moodSad.getId(), result.get(1).getId());
    }

    @Test
    void convertFromMoodsToMoodDTOs_shouldReturnEmptyListForNullMoods() {
        List<MoodDTO> result = MoodConverter.convertFromMoodsToMoodDTOs(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromMoodDTOToMood_shouldReturnMoodWhenDTOIsValid() {
        Mood result = MoodConverter.convertFromMoodDTOToMood(moodDTOHappy);

        assertNotNull(result);
        assertEquals(moodDTOHappy.getId(), result.getId());
        assertEquals(moodDTOHappy.getName(), result.getName());
        assertEquals(moodDTOHappy.getEmoji(), result.getEmoji());
    }

    @Test
    void convertFromMoodDTOToMood_shouldReturnNullForNullDTO() {
        assertNull(MoodConverter.convertFromMoodDTOToMood(null));
    }

    @Test
    void convertFromMoodDTOsToMoods_shouldConvertListSuccessfully() {
        List<MoodDTO> dtos = List.of(moodDTOHappy, moodDTOSad);
        List<Mood> result = MoodConverter.convertFromMoodDTOsToMoods(dtos);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(moodDTOHappy.getId(), result.get(0).getId());
        assertEquals(moodDTOSad.getId(), result.get(1).getId());
    }

    @Test
    void convertFromMoodDTOsToMoods_shouldReturnEmptyListForNullDTOs() {
        List<Mood> result = MoodConverter.convertFromMoodDTOsToMoods(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
