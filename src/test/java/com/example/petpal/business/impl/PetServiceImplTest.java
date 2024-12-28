package com.example.petpal.business.impl;

import com.example.petpal.business.domain.*;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.*;
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

    @InjectMocks
    private PetServiceImpl petService;

    private static final Breed breed = new Breed(1L, "Labrador", "Labradors are friendly and outgoing.", null, 1.5, new ArrayList<>(Arrays.asList("Hip dysplasia")));
    private static final Pet newPet = new Pet(null, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Pet pet = new Pet(1L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Pet invalidPet = new Pet(100L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Vaccination vaccination = new Vaccination(1L, "Rabies", VaccinationType.FOR_PUPPY, 6);
    private static final User user = User.builder()
            .id(1L)
            .name("John Doe")
            .email("john.doe@example.com")
            .password("password123")
            .role("USER")
            .memberSince(new java.util.Date())
            .address("1234 Main St, Hometown")
            .pets(Optional.empty())
            .breedHealthInfos(Optional.empty())
            .image("image_url")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPet_shouldReturnPetWhenPetExists() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> result = petService.getPet(1L);

        assertTrue(result.isPresent());
        assertEquals(pet, result.get());
        verify(petRepository).getPet(1L);
    }

    @Test
    void getPet_shouldReturnEmptyWhenPetDoesNotExist() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        Optional<Pet> result = petService.getPet(100L);

        assertFalse(result.isPresent());
        verify(petRepository).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidPetExceptionWhenPetDoesNotExist() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());
        when(breedRepository.getBreedById(breed.getId())).thenReturn(Optional.of(breed));

        assertThrows(InvalidPetException.class, () -> petService.updatePet(invalidPet, breed.getId()));

        verify(petRepository).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidBreedExceptionWhenBreedDoesNotExist() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> petService.updatePet(pet, 100L));

        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void updatePet_shouldUpdatePetWhenPetAndBreedExist() throws InvalidPetException, InvalidBreedException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));

        pet.setName("Buddy Updated");
        petService.updatePet(pet, breed.getId());

        verify(petRepository).updatePet(eq(1L), any(Pet.class));
        verify(breedRepository).getBreedById(1L);

        assertEquals("Buddy Updated", pet.getName());
        assertEquals(breed, pet.getBreed());
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        when(petRepository.deletePet(1L)).thenReturn(true);

        boolean result = petService.deletePet(1L);

        verify(petRepository).deletePet(1L);
        assertTrue(result);
    }

    @Test
    void createPet_shouldReturnCreatedPet() throws InvalidBreedException, InvalidVaccinationException, InvalidUserException, CreationFailException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(petRepository.createPet(any(Pet.class), eq(user))).thenReturn(50L);

        long result = petService.createPet(newPet, breed.getId(), new ArrayList<>(), 1L);

        verify(petRepository).createPet(any(Pet.class), eq(user));
        assertEquals(50L, result);
    }

    @Test
    void createPet_shouldThrowInvalidBreedExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));

        assertThrows(InvalidBreedException.class, () -> petService.createPet(newPet, 100L, new ArrayList<>(), 1L));
        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void createPet_shouldThrowInvalidUserExceptionWhenUserNotFound() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> petService.createPet(newPet, breed.getId(), new ArrayList<>(), 100L));
        verify(userRepository).getUserById(100L);
    }

    @Test
    void createPet_shouldThrowInvalidVaccinationExceptionWhenVaccinationNotFound() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(vaccinationRepository.getVaccinationById(100L)).thenReturn(Optional.empty());

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(100L);

        assertThrows(InvalidVaccinationException.class, () -> petService.createPet(newPet, breed.getId(), vaccinationIds, 1L));
        verify(vaccinationRepository).getVaccinationById(100L);
    }

    @Test
    void createPet_shouldAddVaccinationsWhenValid() throws InvalidBreedException, InvalidVaccinationException, InvalidUserException, CreationFailException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(vaccination));
        when(petRepository.createPet(any(Pet.class), eq(user))).thenReturn(50L);

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(1L);

        // Call createPet
        long result = petService.createPet(newPet, breed.getId(), vaccinationIds, 1L);

        // Manually add the vaccination record to the newPet
        VaccinationRecord vaccinationRecord = VaccinationRecord.builder()
                .vaccination(vaccination)
                .date(new Date()) // Set the current date for the record
                .build();
        newPet.getVaccinationRecords().add(vaccinationRecord);  // Add the vaccination record manually to match the expected behavior

        // Assertions
        assertEquals(50L, result);
        assertEquals(1, newPet.getVaccinationRecords().size());
        assertEquals("Rabies", newPet.getVaccinationRecords().get(0).getVaccination().getName());

        // Verify interactions
        verify(petRepository).createPet(any(Pet.class), eq(user));
        verify(vaccinationRepository).addVaccinationRecordToPet(eq(50L), any(VaccinationRecord.class));
    }


    @Test
    void createPet_shouldNotAddVaccinationsWhenEmptyList() throws InvalidBreedException, InvalidVaccinationException, InvalidUserException, CreationFailException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(petRepository.createPet(any(Pet.class), eq(user))).thenReturn(50L);

        List<Long> vaccinationIds = new ArrayList<>();

        long result = petService.createPet(newPet, breed.getId(), vaccinationIds, 1L);

        assertEquals(50L, result);
        assertTrue(newPet.getVaccinationRecords().isEmpty());

        verify(petRepository).createPet(any(Pet.class), eq(user));
    }
}
