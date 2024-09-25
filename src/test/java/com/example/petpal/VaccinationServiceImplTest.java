package com.example.petpal;

import com.example.petpal.business.converters.VaccinationConverter;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.impl.VaccinationServiceImpl;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaccinationServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @InjectMocks
    private VaccinationServiceImpl vaccinationService;

    private PetEntity petEntity;
    private VaccinationRecord vaccinationRecord;
    private VaccinationRecordEntity vaccinationRecordEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        petEntity = new PetEntity();
        petEntity.setId(1L);

        Vaccination vaccination = Vaccination.builder()
                .id(1L)
                .name("Rabies")
                .type(VaccinationType.ForPuppy)
                .range(12)
                .build();

        vaccinationRecord = VaccinationRecord.builder()
                .id(1L)
                .vaccination(vaccination)
                .date(new Date())
                .build();

        vaccinationRecordEntity = VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord);
    }

    @Test
    void addVaccinationRecord_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.addVaccinationRecord(100L, vaccinationRecord));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void addVaccinationRecord_shouldAddRecordIfPetExists() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));

        vaccinationService.addVaccinationRecord(1L, vaccinationRecord);

        verify(petRepository, times(1)).addVaccinationToPet(eq(1L), any(VaccinationRecordEntity.class));
    }

    @Test
    void getVaccinationRecordsByPetId_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.getVaccinationRecordsByPetId(100L));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldReturnRecordsIfPetExists() throws InvalidPetException {
        ArrayList<VaccinationRecordEntity> recordEntities = new ArrayList<>();
        recordEntities.add(vaccinationRecordEntity);

        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(petRepository.getVaccinationRecordsByPetId(1L)).thenReturn(recordEntities);

        var result = vaccinationService.getVaccinationRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Rabies", result.get(0).getVaccination().getName());
        assertEquals(VaccinationType.ForPuppy, result.get(0).getVaccination().getType());
        assertEquals(12, result.get(0).getVaccination().getRange());
        verify(petRepository, times(1)).getVaccinationRecordsByPetId(1L);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldReturnEmptyIfNoRecords() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(petRepository.getVaccinationRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = vaccinationService.getVaccinationRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(petRepository, times(1)).getVaccinationRecordsByPetId(1L);
    }
}
