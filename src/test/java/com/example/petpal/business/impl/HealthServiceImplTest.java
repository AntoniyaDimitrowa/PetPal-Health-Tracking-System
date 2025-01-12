package com.example.petpal.business.impl;

import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.*;
import com.example.petpal.controller.dto.health.PetStatisticsDTO;
import com.example.petpal.persistence.*;
import com.example.petpal.configuration.security.token.IAccessToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class HealthServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IHealthRepository healthRepository;

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IMoodRepository moodRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IAccessToken requestAccessToken;

    @InjectMocks
    private HealthServiceImpl healthService;

    private static final Pet pet = Pet.builder().id(1L).build();
    private static final Mood mood = Mood.builder().id(1L).name("Happy").emoji("😊").build();
    private static final Breed breed = Breed.builder().id(1L).name("Labrador").normalMood(mood).build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for getHealthRecordsByPetId
    @Test
    void getHealthRecordsByPetId_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.getHealthRecordsByPetId(100L));

        verify(petRepository).getPet(100L);
    }

    @Test
    void getHealthRecordsByPetId_shouldThrowExceptionIfUnauthorized() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        assertThrows(UnauthorizedDataAccessException.class, () -> healthService.getHealthRecordsByPetId(1L));
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnRecordsIfAuthorized() throws InvalidPetException, UnauthorizedDataAccessException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        List<HealthRecord> records = List.of(HealthRecord.builder().build());
        when(healthRepository.getHealthRecordsByPetId(1L)).thenReturn(records);

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(healthRepository).getHealthRecordsByPetId(1L);
    }

    // Tests for createHealthRecord
    @Test
    void createHealthRecord_shouldThrowExceptionIfUnauthorized() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        assertThrows(UnauthorizedDataAccessException.class, () -> healthService.createHealthRecord(1L, HealthRecord.builder().build(), 1L));
    }

    @Test
    void createHealthRecord_shouldThrowExceptionIfMoodNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(moodRepository.getMoodById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidMoodException.class, () -> healthService.createHealthRecord(1L, HealthRecord.builder().build(), 100L));
    }

    @Test
    void createHealthRecord_shouldCreateRecordIfAuthorizedAndMoodExists() throws InvalidPetException, InvalidMoodException, UnauthorizedDataAccessException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));
        when(healthRepository.createHealthRecordToPet(eq(1L), any(HealthRecord.class))).thenReturn(1L);

        Long id = healthService.createHealthRecord(1L, HealthRecord.builder().build(), 1L);

        assertNotNull(id);
        verify(healthRepository).createHealthRecordToPet(eq(1L), any(HealthRecord.class));
    }

    // Tests for getStatisticsForPet
    @Test
    void getStatisticsForPet_shouldThrowUnauthorizedDataAccessException() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestAccessToken.getUserId()).thenReturn(2L);
        assertThrows(UnauthorizedDataAccessException.class, () -> healthService.getStatisticsForPet(1L, 1, 2025));
    }

    @Test
    void getStatisticsForPet_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.getStatisticsForPet(1L, 1, 2025));
    }

    @Test
    void getStatisticsForPet_shouldReturnStatisticsIfAuthorized() throws InvalidPetException, UnauthorizedDataAccessException {
        // Mocking the pet repository to return a valid pet
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));

        // Mocking the user repository to return the correct owner for the pet
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));

        // Mocking the access token to match the owner's user ID
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // Mocking the health records
        List<Object[]> healthRecords = new ArrayList<>();
        healthRecords.add(new Object[]{
                java.sql.Date.valueOf("2025-01-01"), // Date of health record
                1.0, // Food intake
                2.0, // Water intake
                3, // Activity level
                1.0, // Normal food intake
                2.0, // Normal water intake
                3.0 // Minimum exercise per day
        });
        when(healthRepository.findHealthRecordsWithNormsForPet(1L, 1, 2025)).thenReturn(healthRecords);

        // Mocking the mood distribution
        List<Object[]> moodDistribution = new ArrayList<>();
        moodDistribution.add(new Object[]{
                1L, // Mood ID
                "Happy", // Mood name
                5L // Mood count
        });
        when(healthRepository.findMoodDistributionForPet(1L, 1, 2025)).thenReturn(moodDistribution);

        // Invoking the method to test
        var result = healthService.getStatisticsForPet(1L, 1, 2025);

        // Assertions to validate the behavior
        assertNotNull(result);
        assertEquals(1, result.getFoodIntake().size());
        assertEquals(1, result.getMoodDistribution().size());

        // Validate specific data
        PetStatisticsDTO.FoodWaterIntakeDTO foodIntakeDTO = result.getFoodIntake().get(0);
        assertEquals(1.0, foodIntakeDTO.getIntake());
        assertEquals(1.0, foodIntakeDTO.getNorm());

        PetStatisticsDTO.MoodDistributionDTO moodDTO = result.getMoodDistribution().get(0);
        assertEquals("Happy", moodDTO.getMood());
        assertEquals(5L, moodDTO.getValue());
    }


    // Tests for createHealthInfoForBreed
    @Test
    void createHealthInfoForBreed_shouldThrowExceptionIfBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> healthService.createHealthInfoForBreed(100L, 1L, BreedHealthInfo.builder().build()));
    }

    @Test
    void createHealthInfoForBreed_shouldThrowExceptionIfUnauthorized() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(2L)).thenReturn(Optional.of(User.builder().id(2L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        assertThrows(UnauthorizedDataAccessException.class, () -> healthService.createHealthInfoForBreed(1L, 2L, BreedHealthInfo.builder().build()));
    }

    @Test
    void createHealthInfoForBreed_shouldCreateInfoIfAuthorized() throws InvalidBreedException, UnauthorizedDataAccessException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(healthRepository.createHealthInfoForBreed(any(BreedHealthInfo.class))).thenReturn(1L);

        Long id = healthService.createHealthInfoForBreed(1L, 1L, BreedHealthInfo.builder().build());

        assertNotNull(id);
        verify(healthRepository).createHealthInfoForBreed(any(BreedHealthInfo.class));
    }

    // Tests for getRecentRecords
    @Test
    void getRecentRecords_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.getRecentRecords(1L, 5));
    }

    @Test
    void getRecentRecords_shouldReturnRecordsIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        List<HealthRecord> records = List.of(HealthRecord.builder().build());
        when(healthRepository.getHealthRecentRecordsByPetId(1L, 5)).thenReturn(records);

        var result = healthService.getRecentRecords(1L, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
