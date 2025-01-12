package com.example.petpal.business.impl;

import com.example.petpal.business.domain.*;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.*;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IVaccinationRepository;
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
class PetServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IVaccinationRepository vaccinationRepository;

    @Mock
    private IAccessToken accessToken;

    @InjectMocks
    private PetServiceImpl petService;

    private static final Breed breed = new Breed(1L, "Labrador", "Labradors are friendly and outgoing.", null, 1.5, new ArrayList<>(Arrays.asList("Hip dysplasia")));
    private static final Pet pet = new Pet(1L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .password("password123")
            .role("USER")
            .memberSince(new java.util.Date())
            .address("1234 Main St, Hometown")
            .image("image_url")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPet_shouldThrowUnauthorizedDataAccessExceptionWhenUserUnauthorized() {
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> petService.getPet(1L));

        verify(userRepository).getUserByPetId(1L);
        verifyNoInteractions(petRepository);
    }

    @Test
    void getPet_shouldReturnPetWhenAuthorizedAndExists() throws UnauthorizedDataAccessException {
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(1L);
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> result = petService.getPet(1L);

        assertTrue(result.isPresent());
        assertEquals(pet, result.get());
        verify(userRepository).getUserByPetId(1L);
        verify(petRepository).getPet(1L);
    }

    @Test
    void createPet_shouldThrowUnauthorizedDataAccessExceptionWhenUserUnauthorized() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> petService.createPet(pet, 1L, new ArrayList<>(), 1L));

        verify(userRepository).getUserById(1L);
        verifyNoInteractions(breedRepository, vaccinationRepository, petRepository);
    }

    @Test
    void updatePet_shouldThrowUnauthorizedDataAccessExceptionWhenUserUnauthorized() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> petService.updatePet(pet, 1L));

        verify(userRepository).getUserByPetId(1L);
        verifyNoInteractions(breedRepository);
    }

    @Test
    void updatePet_shouldUpdatePetWhenAuthorized() throws InvalidPetException, InvalidBreedException, UnauthorizedDataAccessException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(1L);
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));

        pet.setName("Updated Name");
        petService.updatePet(pet, 1L);

        verify(petRepository).updatePet(eq(1L), any(Pet.class));
        assertEquals("Updated Name", pet.getName());
    }

    @Test
    void deletePet_shouldThrowUnauthorizedDataAccessExceptionWhenUserUnauthorized() {
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> petService.deletePet(1L));

        verify(userRepository).getUserByPetId(1L);
        verifyNoInteractions(petRepository);
    }

    @Test
    void deletePet_shouldDeletePetWhenAuthorized() throws UnauthorizedDataAccessException {
        when(userRepository.getUserByPetId(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(1L);
        when(petRepository.deletePet(1L)).thenReturn(true);

        boolean result = petService.deletePet(1L);

        assertTrue(result);
        verify(petRepository).deletePet(1L);
    }

    @Test
    void createPet_shouldAddVaccinationRecordsWhenAuthorized() throws Exception {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(1L);
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(new Vaccination(1L, "Rabies", VaccinationType.FOR_PUPPY, 6)));
        when(petRepository.createPet(any(Pet.class), eq(user))).thenReturn(1L);

        List<Long> vaccinationIds = List.of(1L);
        long petId = petService.createPet(pet, 1L, vaccinationIds, 1L);

        assertEquals(1L, petId);
        verify(vaccinationRepository).addVaccinationRecordToPet(eq(1L), any(VaccinationRecord.class));
    }

    @Test
    void createPet_shouldNotAddVaccinationRecordsWhenEmptyList() throws Exception {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(accessToken.getUserId()).thenReturn(1L);
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(petRepository.createPet(any(Pet.class), eq(user))).thenReturn(1L);

        List<Long> vaccinationIds = Collections.emptyList();
        long petId = petService.createPet(pet, 1L, vaccinationIds, 1L);

        assertEquals(1L, petId);
        verify(vaccinationRepository, never()).addVaccinationRecordToPet(anyLong(), any(VaccinationRecord.class));
    }
}
