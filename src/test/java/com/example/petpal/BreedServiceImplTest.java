package com.example.petpal;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.converters.BreedConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.impl.BreedServiceImpl;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BreedServiceImplTest {

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IMoodRepository moodRepository;

    @InjectMocks
    private BreedServiceImpl breedService;

    private MoodEntity moodEntity;
    private BreedEntity breedEntity;
    private Breed breed;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        moodEntity = MoodEntity.builder()
                .id(1L)
                .name("Happy")
                .emoji("")
                .build();

        breedEntity = BreedEntity.builder()
                .id(1L)
                .name("Labrador")
                .description("Friendly, outgoing.")
                .normalMood(moodEntity)
                .minimumExercisePerDay(1.5)
                .build();
        breed = BreedConverter.convertFromBreedEntityToBreed(breedEntity);
    }

    @Test
    void getAllBreeds_shouldReturnAllBreeds() {
        ArrayList<Breed> breeds = new ArrayList<>();
        breeds.add(breed);
        when(breedRepository.getAllBreeds()).thenReturn(breeds);

        ArrayList<Breed> result = breedService.getAllBreeds();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(breedRepository, times(1)).getAllBreeds();
    }

    @Test
    void getBreedById_shouldReturnBreedWhenExists() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));

        Optional<Breed> result = breedService.getBreedById(1L);

        assertTrue(result.isPresent());
        assertEquals(breed, result.get());
        verify(breedRepository, times(1)).getBreedById(1L);
    }

    @Test
    void getBreedById_shouldReturnEmptyWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        Optional<Breed> result = breedService.getBreedById(100L);

        assertFalse(result.isPresent());
        verify(breedRepository, times(1)).getBreedById(100L);
    }

    @Test
    void updateBreed_shouldThrowExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        Breed breedWithNonExistingId = Breed.builder().id(100L).build();
        assertThrows(InvalidBreedException.class, () -> {
            breedService.updateBreed(breedWithNonExistingId, 100L);
        });

        verify(breedRepository, times(1)).getBreedById(100L);
    }


    @Test
    void updateBreed_shouldThrowInvalidMoodExceptionWhenMoodNotFound() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(moodRepository.getMoodById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidMoodException.class, () -> breedService.updateBreed(breed, 100L));

        verify(breedRepository, times(1)).getBreedById(1L);
        verify(moodRepository, times(1)).getMoodById(100L);
    }

    @Test
    void updateBreed_shouldUpdateBreedWhenExistsAndMoodIsValid() throws InvalidBreedException, InvalidMoodException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(Mood.builder().build()));
        when(breedRepository.updateBreed(eq(1L), any(Breed.class))).thenReturn(breed);

        Breed result = breedService.updateBreed(breed, 1L);

        assertNotNull(result);
        verify(breedRepository, times(1)).updateBreed(eq(1L), any(Breed.class));
        verify(moodRepository, times(1)).getMoodById(1L);
    }

    @Test
    void deleteBreed_shouldCallRepositoryDelete() {
        when(breedRepository.deleteBreed(1L)).thenReturn(true);

        boolean result = breedService.deleteBreed(1L);

        assertTrue(result);
        verify(breedRepository, times(1)).deleteBreed(1L);
    }

    @Test
    void getHealthInfoForBreed_shouldThrowExceptionIfBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> breedService.getHealthInfoForBreed(100L, 2));
        verify(breedRepository, times(1)).getBreedById(100L);
    }
}
