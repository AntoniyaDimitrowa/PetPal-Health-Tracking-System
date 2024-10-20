package com.example.petpal;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.impl.HealthServiceImpl;
import com.example.petpal.persistence.IHealthRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.PetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IHealthRepository healthRepository;

    @InjectMocks
    private HealthServiceImpl healthService;

    private PetEntity petEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        petEntity = PetEntity.builder().build();
        petEntity.setId(1L);
    }

    @Test
    void createHealthRecord_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.createHealthRecord(100L, HealthRecord.builder().build()));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void createHealthRecord_shouldCreateRecordIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(healthRepository.createHealthRecordToPet(eq(1L), any())).thenReturn(1L); // Mock the return value

        Long id = healthService.createHealthRecord(1L, HealthRecord.builder().build());

        assertNotNull(id);
        verify(healthRepository, times(1)).createHealthRecordToPet(eq(1L), any());
    }

    @Test
    void getHealthRecordsByPetId_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.getHealthRecordsByPetId(100L));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnRecordsIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(healthRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        verify(healthRepository, times(1)).getHealthRecordsByPetId(1L);
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnEmptyIfNoRecords() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(healthRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(healthRepository, times(1)).getHealthRecordsByPetId(1L);
    }

    @Test
    void getHealthInfoForBreed_shouldReturnInfoIfExists() {
        Long breedId = 1L;
        int age = 2;

        BreedHealthInfo healthInfo = BreedHealthInfo.builder().build(); // Create a sample health info

        when(healthRepository.getHealthInfoForBreed(breedId, age)).thenReturn(Optional.of(healthInfo));

        var result = healthService.getHealthInfoForBreed(breedId, age);

        assertNotNull(result);
        verify(healthRepository, times(1)).getHealthInfoForBreed(breedId, age);
    }

    @Test
    void getHealthInfoForBreed_shouldReturnNullIfNotExists() {
        Long breedId = 1L;
        int age = 2;

        when(healthRepository.getHealthInfoForBreed(breedId, age)).thenReturn(Optional.empty());

        var result = healthService.getHealthInfoForBreed(breedId, age);

        assertNull(result);
        verify(healthRepository, times(1)).getHealthInfoForBreed(breedId, age);
    }

    @Test
    void createHealthInfoForBreed_shouldCreateInfoIfSuccessful() {
        Long breedId = 1L;
        int ageRangeStart = 1;
        int ageRangeEnd = 5;
        Breed breed = Breed.builder()
                .id(1L)
                .name("Golden Retriever")
                .description("Friendly and intelligent breed.")
                .normalMood(null)
                .minimumExercisePerDay(60)
                .commonHealthProblems(new ArrayList<>())
                .build();

        BreedHealthInfo breedHealthInfo = BreedHealthInfo.builder()
                .breed(breed)
                .ageRangeStart(1)
                .ageRangeEnd(5)
                .normalFoodIntake(500)
                .normalWaterIntake(1.5)
                .build();

        when(healthRepository.createHealthInfoForBreed(breedId, ageRangeStart, ageRangeEnd, breedHealthInfo)).thenReturn(1L);

        Long id = healthService.createHealthInfoForBreed(breedId, ageRangeStart, ageRangeEnd, breedHealthInfo);

        assertNotNull(id);
        verify(healthRepository, times(1)).createHealthInfoForBreed(breedId, ageRangeStart, ageRangeEnd, breedHealthInfo);
    }

    @Test
    void getHealthInfoByBreedId_shouldReturnInfo() {
        Long breedId = 1L;
        ArrayList<BreedHealthInfo> healthInfoList = new ArrayList<>();

        when(healthRepository.getHealthInfoByBreedId(breedId)).thenReturn(healthInfoList);

        var result = healthService.getHealthInfoByBreedId(breedId);

        assertNotNull(result);
        assertEquals(healthInfoList.size(), result.size());
        verify(healthRepository, times(1)).getHealthInfoByBreedId(breedId);
    }
}
