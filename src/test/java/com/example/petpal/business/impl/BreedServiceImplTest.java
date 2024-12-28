package com.example.petpal.business.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class BreedServiceImplTest {

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IMoodRepository moodRepository;

    @InjectMocks
    private BreedServiceImpl breedService;

    private static final Mood mood = Mood.builder().id(1L).name("Happy").emoji("😊").build();

    private static final Breed breed = Breed.builder().id(1L).name("Labrador").description("Friendly, outgoing.")
            .normalMood(mood).minimumExercisePerDay(1.5).build();

    private static final Breed invalidBreed = Breed.builder().id(100L).name("Labrador").description("Friendly, outgoing.")
            .normalMood(mood).minimumExercisePerDay(1.5).build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBreeds_shouldReturnAllBreeds() {
        List<Breed> breeds = List.of(breed);
        when(breedRepository.getAllBreeds()).thenReturn(breeds);

        List<Breed> result = breedService.getAllBreeds();

        assertEquals(breeds, result);
        verify(breedRepository).getAllBreeds();
    }

    @Test
    void getBreedById_shouldReturnBreedWhenExists() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));

        Optional<Breed> result = breedService.getBreedById(1L);

        assertTrue(result.isPresent());
        assertEquals(breed, result.get());
        verify(breedRepository).getBreedById(1L);
    }

    @Test
    void getBreedById_shouldReturnEmptyWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        Optional<Breed> result = breedService.getBreedById(100L);

        assertTrue(result.isEmpty());
        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void createBreed_shouldThrowInvalidMoodExceptionWhenMoodNotFound() {
        when(moodRepository.getMoodById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidMoodException.class, () -> breedService.createBreed(breed, 100L));
        verify(moodRepository).getMoodById(100L);
    }

    @Test
    void createBreed_shouldCreateBreedWhenMoodExists() throws InvalidMoodException {
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));
        when(breedRepository.createBreed(breed)).thenReturn(1L);

        Long result = breedService.createBreed(breed, 1L);

        assertEquals(1L, result);
        verify(breedRepository).createBreed(breed);
        verify(moodRepository).getMoodById(1L);
    }

    @Test
    void updateBreed_shouldThrowExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> breedService.updateBreed(100L, invalidBreed, 1L));
        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void updateBreed_shouldUpdateBreedWhenExistsAndMoodIsValid() throws InvalidBreedException, InvalidMoodException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));
        when(breedRepository.updateBreed(eq(1L), any(Breed.class))).thenReturn(breed);

        Breed result = breedService.updateBreed(1L, breed, 1L);

        assertEquals(breed, result);
        verify(breedRepository).updateBreed(eq(1L), any(Breed.class));
        verify(moodRepository).getMoodById(1L);
    }

    @Test
    void deleteBreed_shouldCallRepositoryDelete() {
        when(breedRepository.deleteBreed(1L)).thenReturn(true);

        boolean result = breedService.deleteBreed(1L);

        assertTrue(result);
        verify(breedRepository).deleteBreed(1L);
    }

    @Test
    void updateHealthProblems_shouldThrowInvalidBreedExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> breedService.updateHealthProblems(100L, new ArrayList<>()));
        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void updateHealthProblems_shouldUpdateHealthProblemsWhenBreedExists() throws InvalidBreedException {
        List<String> healthProblems = List.of("Hip Dysplasia");
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(breedRepository.updateHealthProblems(1L, healthProblems)).thenReturn(breed);

        Breed result = breedService.updateHealthProblems(1L, healthProblems);

        assertEquals(breed, result);
        verify(breedRepository).updateHealthProblems(1L, healthProblems);
    }
}
