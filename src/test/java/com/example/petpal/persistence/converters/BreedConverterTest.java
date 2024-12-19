package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class BreedConverterTest {

    private static final Mood mood = Mood.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();

    private static final MoodEntity moodEntity = MoodEntity.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();

    private static final Breed breed = Breed.builder()
            .id(1L)
            .name("Labrador")
            .description("Friendly, outgoing.")
            .normalMood(mood)
            .minimumExercisePerDay(1.5)
            .commonHealthProblems(List.of("Hip Dysplasia"))
            .build();

    private static final BreedEntity breedEntity = BreedEntity.builder()
            .id(1L)
            .name("Labrador")
            .description("Friendly, outgoing.")
            .normalMood(moodEntity)
            .minimumExercisePerDay(1.5)
            .commonHealthProblems(List.of("Hip Dysplasia"))
            .build();

    @Test
    void convertFromBreedToBreedEntity_shouldReturnBreedEntity() {
        BreedEntity result = BreedConverter.convertFromBreedToBreedEntity(breed);

        assertNotNull(result);
        assertEquals(breed.getId(), result.getId());
        assertEquals(breed.getName(), result.getName());
        assertEquals(breed.getDescription(), result.getDescription());
        assertEquals(breed.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(breed.getCommonHealthProblems(), result.getCommonHealthProblems());
        assertEquals(breed.getNormalMood().getId(), result.getNormalMood().getId());
    }

    @Test
    void convertFromBreedToBreedEntity_shouldReturnNullForNullBreed() {
        assertNull(BreedConverter.convertFromBreedToBreedEntity(null));
    }

    @Test
    void convertFromBreedEntityToBreed_shouldReturnBreed() {
        Breed result = BreedConverter.convertFromBreedEntityToBreed(breedEntity);

        assertNotNull(result);
        assertEquals(breedEntity.getId(), result.getId());
        assertEquals(breedEntity.getName(), result.getName());
        assertEquals(breedEntity.getDescription(), result.getDescription());
        assertEquals(breedEntity.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(breedEntity.getCommonHealthProblems(), result.getCommonHealthProblems());
        assertEquals(breedEntity.getNormalMood().getId(), result.getNormalMood().getId());
    }

    @Test
    void convertFromBreedEntityToBreed_shouldReturnNullForNullEntity() {
        assertNull(BreedConverter.convertFromBreedEntityToBreed(null));
    }

    @Test
    void convertFromBreedsToBreedEntities_shouldReturnListOfBreedEntities() {
        List<Breed> breeds = List.of(breed);
        List<BreedEntity> result = BreedConverter.convertFromBreedsToBreedEntities(breeds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breed.getId(), result.get(0).getId());
    }

    @Test
    void convertFromBreedsToBreedEntities_shouldReturnEmptyListForNullInput() {
        List<BreedEntity> result = BreedConverter.convertFromBreedsToBreedEntities(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromBreedEntitiesToBreeds_shouldReturnListOfBreeds() {
        List<BreedEntity> breedEntities = List.of(breedEntity);
        List<Breed> result = BreedConverter.convertFromBreedEntitiesToBreeds(breedEntities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breedEntity.getId(), result.get(0).getId());
    }

    @Test
    void convertFromBreedEntitiesToBreeds_shouldReturnEmptyListForNullInput() {
        List<Breed> result = BreedConverter.convertFromBreedEntitiesToBreeds(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
