package com.example.petpal;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.business.impl.VaccinationServiceImpl;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.persistence.entity.PetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaccinationServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IVaccinationRepository vaccinationRepository;

    @InjectMocks
    private VaccinationServiceImpl vaccinationService;

    private PetEntity petEntity;
    private VaccinationRecord vaccinationRecord;
    private Pet pet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        petEntity = PetEntity.builder().id(1L).build();
        pet = PetConverter.convertFromPetEntityToPet(petEntity); // Convert to domain object for repository calls

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
    }

    @Test
    void createVaccinationRecord_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.createVaccinationRecord(100L, vaccinationRecord));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void createVaccinationRecord_shouldThrowExceptionIfVaccinationNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidVaccinationException.class, () -> vaccinationService.createVaccinationRecord(1L, vaccinationRecord));
        verify(vaccinationRepository, times(1)).getVaccinationById(1L);
    }

    @Test
    void createVaccinationRecord_shouldCreateRecordIfPetAndVaccinationExist() throws InvalidPetException, InvalidVaccinationException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(Vaccination.builder().build()));

        vaccinationService.createVaccinationRecord(1L, vaccinationRecord);

        verify(vaccinationRepository, times(1)).addVaccinationRecordToPet(eq(pet), any(VaccinationRecord.class));
        verify(vaccinationRepository, times(1)).getVaccinationById(1L);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldThrowExceptionIfPetNotFound() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.getVaccinationRecordsByPetId(100L));
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldReturnRecordsIfPetExists() throws InvalidPetException {
        List<VaccinationRecord> vaccinationRecords = new ArrayList<>();
        vaccinationRecords.add(vaccinationRecord);

        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(vaccinationRepository.getVaccinationRecordsByPetId(1L)).thenReturn(vaccinationRecords);

        var result = vaccinationService.getVaccinationRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Rabies", result.get(0).getVaccination().getName());
        assertEquals(VaccinationType.ForPuppy, result.get(0).getVaccination().getType());
        assertEquals(12, result.get(0).getVaccination().getRange());
        verify(vaccinationRepository, times(1)).getVaccinationRecordsByPetId(1L);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldReturnEmptyIfNoRecords() throws InvalidPetException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(vaccinationRepository.getVaccinationRecordsByPetId(1L)).thenReturn(new ArrayList<>());

        var result = vaccinationService.getVaccinationRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(vaccinationRepository, times(1)).getVaccinationRecordsByPetId(1L);
    }

    @Test
    void getVaccinations_shouldReturnAllVaccinations() {
        List<Vaccination> vaccinations = new ArrayList<>();
        vaccinations.add(Vaccination.builder().id(1L).name("Rabies").build());

        when(vaccinationRepository.getAllVaccinations()).thenReturn(vaccinations);

        var result = vaccinationService.getVaccinations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Rabies", result.get(0).getName());
        verify(vaccinationRepository, times(1)).getAllVaccinations();
    }
}
