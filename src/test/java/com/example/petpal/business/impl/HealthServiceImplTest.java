package com.example.petpal.business.impl;

import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.IPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IHealthRepository healthRepository;

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IMoodRepository moodRepository;

    @InjectMocks
    private HealthServiceImpl healthService;

    private static final Pet pet = Pet.builder().id(1L).build();

    private static final Mood mood = Mood.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();

    private static final Breed breed = Breed.builder().id(1L).name("Labrador").normalMood(mood).build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for createHealthRecord
    @Test
    void createHealthRecord_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));

        assertThrows(InvalidPetException.class, () -> healthService.createHealthRecord(100L, HealthRecord.builder().build(), 1L));

        verify(petRepository).getPet(100L);
        verifyNoInteractions(healthRepository);
    }

    @Test
    void createHealthRecord_shouldThrowExceptionIfMoodNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(moodRepository.getMoodById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidMoodException.class, () -> healthService.createHealthRecord(1L, HealthRecord.builder().build(), 100L));

        verify(petRepository).getPet(1L);
        verify(moodRepository).getMoodById(100L);
        verifyNoInteractions(healthRepository);
    }

    @Test
    void createHealthRecord_shouldCreateRecordIfPetExists() throws InvalidPetException, InvalidMoodException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));
        when(healthRepository.createHealthRecordToPet(eq(1L), any(HealthRecord.class))).thenReturn(1L);

        Long id = healthService.createHealthRecord(1L, HealthRecord.builder().build(), 1L);

        assertNotNull(id);
        verify(healthRepository).createHealthRecordToPet(eq(1L), any(HealthRecord.class));
    }

    // Tests for getHealthRecordsByPetId
    @Test
    void getHealthRecordsByPetId_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.getHealthRecordsByPetId(100L));

        verify(petRepository).getPet(100L);
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnRecordsIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(healthRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        verify(healthRepository).getHealthRecordsByPetId(1L);
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnEmptyIfNoRecords() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(healthRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(healthRepository).getHealthRecordsByPetId(1L);
    }

    // Tests for getHealthInfoForBreed
    @Test
    void getHealthInfoForBreed_shouldReturnInfoIfExists() {
        Long breedId = 1L;
        int age = 2;
        BreedHealthInfo healthInfo = BreedHealthInfo.builder().build();

        when(healthRepository.getHealthInfoForBreed(breedId, age)).thenReturn(Optional.of(healthInfo));

        var result = healthService.getHealthInfoForBreed(breedId, age);

        assertNotNull(result);
        verify(healthRepository).getHealthInfoForBreed(breedId, age);
    }

    @Test
    void getHealthInfoForBreed_shouldReturnNullIfNotExists() {
        Long breedId = 1L;
        int age = 2;

        when(healthRepository.getHealthInfoForBreed(breedId, age)).thenReturn(Optional.empty());

        var result = healthService.getHealthInfoForBreed(breedId, age);

        assertNull(result);
        verify(healthRepository).getHealthInfoForBreed(breedId, age);
    }

    // Tests for createHealthInfoForBreed
    @Test
    void createHealthInfoForBreed_shouldThrowExceptionIfBreedNotFound() {
        Long breedId = 100L;
        int ageRangeStart = 1;
        int ageRangeEnd = 5;
        BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder().build();

        when(breedRepository.getBreedById(breedId)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> healthService.createHealthInfoForBreed(breedId, breedHealthInfo));

        verify(breedRepository).getBreedById(breedId);
    }

    @Test
    void createHealthInfoForBreed_shouldCreateInfoIfSuccessful() throws InvalidBreedException {
        Long breedId = 1L;
        int ageRangeStart = 1;
        int ageRangeEnd = 5;
        BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder().build();

        when(breedRepository.getBreedById(breedId)).thenReturn(Optional.of(breed));
        when(healthRepository.createHealthInfoForBreed(breedHealthInfo)).thenReturn(1L);

        Long id = healthService.createHealthInfoForBreed(breedId, breedHealthInfo);

        assertNotNull(id);
        verify(healthRepository).createHealthInfoForBreed(breedHealthInfo);
    }

    // Tests for getHealthInfoByBreedId
    @Test
    void getHealthInfoByBreedId_shouldReturnInfo() {
        Long breedId = 1L;
        List<BreedHealthInfo> healthInfoList = new ArrayList<>();

        when(healthRepository.getHealthInfoByBreedId(breedId)).thenReturn(healthInfoList);

        var result = healthService.getHealthInfoByBreedId(breedId);

        assertNotNull(result);
        assertEquals(healthInfoList.size(), result.size());
        verify(healthRepository).getHealthInfoByBreedId(breedId);
    }

    @Test
    void getHealthInfoByBreedId_shouldReturnEmptyListIfNoInfoExists() {
        Long breedId = 1L;

        when(healthRepository.getHealthInfoByBreedId(breedId)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthInfoByBreedId(breedId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(healthRepository).getHealthInfoByBreedId(breedId);
    }
}
