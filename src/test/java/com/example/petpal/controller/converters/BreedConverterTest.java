package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.dto.breed.BreedDTO;
import com.example.petpal.controller.dto.breed.CreateBreedDTO;
import com.example.petpal.controller.dto.breed.UpdateBreedDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class BreedConverterTest {

    private static final Mood mood = Mood.builder().id(1L).name("Happy").emoji("😊").build();
    private static final Breed breed = Breed.builder()
            .id(1L)
            .name("Labrador")
            .description("Friendly, outgoing.")
            .normalMood(mood)
            .minimumExercisePerDay(1.5)
            .commonHealthProblems(List.of("Hip Dysplasia"))
            .build();

    private static final BreedDTO breedDTO = BreedDTO.builder()
            .id(1L)
            .name("Labrador")
            .description("Friendly, outgoing.")
            .normalMood(MoodConverter.convertFromMoodToMoodDTO(mood))
            .minimumExercisePerDay(1.5)
            .commonHealthProblems(List.of("Hip Dysplasia"))
            .build();

    @Test
    void convertFromBreedToBreedDTO_shouldReturnBreedDTO() {
        BreedDTO result = BreedConverter.convertFromBreedToBreedDTO(breed);

        assertNotNull(result);
        assertEquals(breed.getId(), result.getId());
        assertEquals(breed.getName(), result.getName());
        assertEquals(breed.getDescription(), result.getDescription());
        assertEquals(breed.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(breed.getCommonHealthProblems(), result.getCommonHealthProblems());
        assertEquals(breed.getNormalMood().getId(), result.getNormalMood().getId());
    }

    @Test
    void convertFromBreedToBreedDTO_shouldReturnNullForNullBreed() {
        assertNull(BreedConverter.convertFromBreedToBreedDTO(null));
    }

    @Test
    void convertFromBreedDTOToBreed_shouldReturnBreed() {
        Breed result = BreedConverter.convertFromBreedDTOToBreed(breedDTO);

        assertNotNull(result);
        assertEquals(breedDTO.getId(), result.getId());
        assertEquals(breedDTO.getName(), result.getName());
        assertEquals(breedDTO.getDescription(), result.getDescription());
        assertEquals(breedDTO.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(breedDTO.getCommonHealthProblems(), result.getCommonHealthProblems());
        assertEquals(breedDTO.getNormalMood().getId(), result.getNormalMood().getId());
    }

    @Test
    void convertFromBreedDTOToBreed_shouldReturnNullForNullDTO() {
        assertNull(BreedConverter.convertFromBreedDTOToBreed(null));
    }

    @Test
    void convertFromBreedsToBreedDTOs_shouldReturnListOfBreedDTOs() {
        List<Breed> breeds = List.of(breed);
        List<BreedDTO> result = BreedConverter.convertFromBreedsToBreedDTOs(breeds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(breed.getId(), result.get(0).getId());
    }

    @Test
    void convertFromBreedsToBreedDTOs_shouldReturnEmptyListForNullInput() {
        List<BreedDTO> result = BreedConverter.convertFromBreedsToBreedDTOs(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromCreateBreedDTOToBreed_shouldReturnBreed() {
        CreateBreedDTO createBreedDTO = CreateBreedDTO.builder()
                .name("Labrador")
                .description("Friendly, outgoing.")
                .minimumExercisePerDay(1.5)
                .commonHealthProblems(List.of("Hip Dysplasia"))
                .build();

        Breed result = BreedConverter.convertFromCreateBreedDTOToBreed(createBreedDTO);

        assertNotNull(result);
        assertEquals(createBreedDTO.getName(), result.getName());
        assertEquals(createBreedDTO.getDescription(), result.getDescription());
        assertEquals(createBreedDTO.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(createBreedDTO.getCommonHealthProblems(), result.getCommonHealthProblems());
    }

    @Test
    void convertFromUpdateBreedDTOToBreed_shouldReturnBreed() {
        UpdateBreedDTO updateBreedDTO = UpdateBreedDTO.builder()
                .name("Labrador")
                .description("Friendly, outgoing.")
                .minimumExercisePerDay(1.5)
                .commonHealthProblems(List.of("Hip Dysplasia"))
                .build();

        Breed result = BreedConverter.convertFromUpdateBreedDTOToBreed(updateBreedDTO);

        assertNotNull(result);
        assertEquals(updateBreedDTO.getName(), result.getName());
        assertEquals(updateBreedDTO.getDescription(), result.getDescription());
        assertEquals(updateBreedDTO.getMinimumExercisePerDay(), result.getMinimumExercisePerDay());
        assertEquals(updateBreedDTO.getCommonHealthProblems(), result.getCommonHealthProblems());
    }
}
