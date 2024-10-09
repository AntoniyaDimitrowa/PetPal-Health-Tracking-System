package com.example.petpal;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.impl.HealthServiceImpl;
import com.example.petpal.persistence.IPetRepository;
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

    @InjectMocks
    private HealthServiceImpl healthService;

    private PetEntity petEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        petEntity = new PetEntity();
        petEntity.setId(1L);
    }

    @Test
    void addHealthRecord_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> healthService.createHealthRecord(100L, new HealthRecord()));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void createHealthRecord_shouldCreateRecordIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));

        healthService.createHealthRecord(1L, new HealthRecord());

        verify(petRepository, times(1)).addHealthRecordToPet(eq(1L), any());
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
        when(petRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        verify(petRepository, times(1)).getHealthRecordsByPetId(1L);
    }

    @Test
    void getHealthRecordsByPetId_shouldReturnEmptyIfNoRecords() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(petRepository.getHealthRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = healthService.getHealthRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(petRepository, times(1)).getHealthRecordsByPetId(1L);
    }
}
