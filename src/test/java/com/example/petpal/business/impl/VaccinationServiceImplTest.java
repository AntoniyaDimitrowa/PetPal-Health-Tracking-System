package com.example.petpal.business.impl;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class VaccinationServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IVaccinationRepository vaccinationRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IAccessToken requestAccessToken;

    @InjectMocks
    private VaccinationServiceImpl vaccinationService;

    private static final Vaccination vaccination = Vaccination.builder()
            .id(1L)
            .name("Rabies")
            .type(VaccinationType.FOR_PUPPY)
            .range(12)
            .build();

    private static final VaccinationRecord vaccinationRecord = VaccinationRecord.builder()
            .id(1L)
            .vaccination(vaccination)
            .date(new Date())
            .build();

    private static final Pet pet = Pet.builder()
            .id(1L)
            .name("Buddy")
            .build();

    private static final User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldThrowInvalidPetExceptionIfPetNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.getVaccinationRecordsByPetId(1L));

        verify(petRepository, times(1)).getPet(1L);
        verifyNoInteractions(vaccinationRepository);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldThrowUnauthorizedDataAccessExceptionIfNotOwner() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(2L); // Not the owner

        assertThrows(UnauthorizedDataAccessException.class, () -> vaccinationService.getVaccinationRecordsByPetId(1L));

        verify(userRepository, times(1)).getUserByPetId(1L);
        verifyNoInteractions(vaccinationRepository);
    }

    @Test
    void getVaccinationRecordsByPetId_shouldReturnRecordsIfAuthorized() throws InvalidPetException, UnauthorizedDataAccessException {
        List<VaccinationRecord> records = List.of(vaccinationRecord);

        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L); // Owner's ID
        when(vaccinationRepository.getVaccinationRecordsByPetId(1L)).thenReturn(records);

        List<VaccinationRecord> result = vaccinationService.getVaccinationRecordsByPetId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vaccination, result.get(0).getVaccination());

        verify(vaccinationRepository, times(1)).getVaccinationRecordsByPetId(1L);
    }

    @Test
    void createVaccinationRecord_shouldThrowInvalidPetExceptionIfPetNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidPetException.class, () -> vaccinationService.createVaccinationRecord(1L, 1L, new Date()));

        verify(petRepository, times(1)).getPet(1L);
        verifyNoInteractions(vaccinationRepository);
    }

    @Test
    void createVaccinationRecord_shouldThrowUnauthorizedDataAccessExceptionIfNotOwner() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(2L); // Not the owner

        assertThrows(UnauthorizedDataAccessException.class, () -> vaccinationService.createVaccinationRecord(1L, 1L, new Date()));

        verify(userRepository, times(1)).getUserByPetId(1L);
        verifyNoInteractions(vaccinationRepository);
    }

    @Test
    void createVaccinationRecord_shouldThrowInvalidVaccinationExceptionIfVaccinationNotFound() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L); // Owner's ID
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidVaccinationException.class, () -> vaccinationService.createVaccinationRecord(1L, 1L, new Date()));

        verify(vaccinationRepository, times(1)).getVaccinationById(1L);
    }

    @Test
    void createVaccinationRecord_shouldCreateRecordIfValid() throws InvalidPetException, InvalidVaccinationException, UnauthorizedDataAccessException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(requestAccessToken.getUserId()).thenReturn(1L); // Owner's ID
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(vaccination));
        when(vaccinationRepository.addVaccinationRecordToPet(eq(1L), any(VaccinationRecord.class))).thenReturn(1L);

        Long recordId = vaccinationService.createVaccinationRecord(1L, 1L, new Date());

        assertNotNull(recordId);
        assertEquals(1L, recordId);

        verify(vaccinationRepository, times(1)).addVaccinationRecordToPet(eq(1L), any(VaccinationRecord.class));
    }

    @Test
    void getVaccinations_shouldReturnAllVaccinations() {
        List<Vaccination> vaccinations = List.of(vaccination);

        when(vaccinationRepository.getAllVaccinations()).thenReturn(vaccinations);

        List<Vaccination> result = vaccinationService.getVaccinations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vaccination, result.get(0));

        verify(vaccinationRepository, times(1)).getAllVaccinations();
    }
}
