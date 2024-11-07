package com.example.petpal.business.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IBreedRepository breedRepository;

    @Mock
    private IVaccinationRepository vaccinationRepository;

    @InjectMocks
    private PetServiceImpl petService;

    // Static data setup (not required for each test)
    private static final Breed breed = new Breed(1L, "Labrador", "Labradors are friendly and outgoing.", null, 1.5, new ArrayList<>(Arrays.asList("Hip dysplasia")));

    private static final Pet newPet = new Pet(null, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Pet pet = new Pet(1L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Pet invalidPet = new Pet(100L, "Buddy", breed, Gender.MALE, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    private static final Vaccination vaccination = new Vaccination(1L, "Rabies", VaccinationType.FOR_PUPPY, 6);

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

        assertEquals("Buddy Updated", pet.getName()); // Verify the name was updated
        assertEquals(breed, pet.getBreed()); // Verify that the breed is correctly set
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        when(petRepository.deletePet(1L)).thenReturn(true);

        boolean result = petService.deletePet(1L);

        verify(petRepository).deletePet(1L);
        assertTrue(result);
    }

    @Test
    void createPet_shouldReturnCreatedPet() throws InvalidBreedException, InvalidVaccinationException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(petRepository.createPet(any(Pet.class))).thenReturn(50L);

        long result = petService.createPet(newPet, breed.getId(), new ArrayList<>());

        verify(petRepository).createPet(any(Pet.class));  // Verifies that petRepository.createPet() was called
        assertEquals(50L, result); // Ensures the returned ID matches the mocked result
    }

    @Test
    void createPet_shouldThrowInvalidBreedExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> petService.createPet(newPet, 100L, new ArrayList<>()));
        verify(breedRepository).getBreedById(100L);
    }

    @Test
    void createPet_shouldThrowInvalidVaccinationExceptionWhenVaccinationNotFound() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(vaccinationRepository.getVaccinationById(100L)).thenReturn(Optional.empty());

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(100L);

        assertThrows(InvalidVaccinationException.class, () -> petService.createPet(newPet, breed.getId(), vaccinationIds));
        verify(vaccinationRepository).getVaccinationById(100L);
    }

    @Test
    void createPet_shouldAddVaccinationsWhenValid() throws InvalidBreedException, InvalidVaccinationException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(vaccination));
        when(petRepository.createPet(any(Pet.class))).thenReturn(50L);

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(1L);

        long result = petService.createPet(newPet, breed.getId(), vaccinationIds);

        assertEquals(50L, result);
        assertEquals(1, newPet.getVaccinationRecords().size());
        assertEquals("Rabies", newPet.getVaccinationRecords().get(0).getVaccination().getName());
        verify(petRepository).createPet(any(Pet.class));
    }
}
