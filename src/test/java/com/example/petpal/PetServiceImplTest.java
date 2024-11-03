package com.example.petpal;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.business.impl.PetServiceImpl;
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

    private Pet pet;
    private Breed breed;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        breed = new Breed(1L, "Labrador", "Labradors are friendly and outgoing.", null, 1.5, new ArrayList<>(Arrays.asList("Hip dysplasia")));
        pet = new Pet(1L, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void getPet_shouldReturnPetWhenPetExists() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> result = petService.getPet(1L);

        assertTrue(result.isPresent());
        assertEquals(pet, result.get());
        verify(petRepository, times(1)).getPet(1L);
    }

    @Test
    void getPet_shouldReturnEmptyWhenPetDoesNotExist() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        Optional<Pet> result = petService.getPet(100L);

        assertFalse(result.isPresent());
        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidPetExceptionWhenPetDoesNotExist() {
        when(petRepository.getPet(100L)).thenReturn(Optional.empty());

        Pet newPet = new Pet(100L, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
        assertThrows(InvalidPetException.class, () -> petService.updatePet(newPet, breed.getId()));

        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidBreedExceptionWhenBreedDoesNotExist() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> petService.updatePet(pet, 100L));

        verify(breedRepository, times(1)).getBreedById(100L);
    }

    @Test
    void updatePet_shouldUpdatePetWhenPetAndBreedExist() throws InvalidPetException, InvalidBreedException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(pet));
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));

        Pet updatedPet = pet;
        updatedPet.setName("Buddy Updated");

        petService.updatePet(updatedPet, breed.getId());

        verify(petRepository, times(1)).updatePet(eq(1L), any(Pet.class));
        verify(breedRepository, times(1)).getBreedById(1L);
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        petService.deletePet(1L);

        verify(petRepository, times(1)).deletePet(1L);
    }

    @Test
    void createPet_shouldReturnCreatedPet() throws InvalidBreedException, InvalidVaccinationException {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(petRepository.createPet(any(Pet.class))).thenReturn(50L);

        Pet newPet = new Pet(null, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());

        long result = petService.createPet(newPet, breed.getId(), new ArrayList<>());

        verify(petRepository, times(1)).createPet(any(Pet.class));
        assertEquals(50L, result);
    }

    @Test
    void createPet_shouldThrowInvalidBreedExceptionWhenBreedNotFound() {
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        Pet newPet = new Pet(null, "Buddy", null, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());

        assertThrows(InvalidBreedException.class, () -> petService.createPet(newPet, 100L, new ArrayList<>()));
        verify(breedRepository, times(1)).getBreedById(100L);
    }

    @Test
    void createPet_shouldThrowInvalidVaccinationExceptionWhenVaccinationNotFound() {
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(vaccinationRepository.getVaccinationById(100L)).thenReturn(Optional.empty());

        Pet newPet = new Pet(null, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(100L);

        assertThrows(InvalidVaccinationException.class, () -> petService.createPet(newPet, breed.getId(), vaccinationIds));
        verify(vaccinationRepository, times(1)).getVaccinationById(100L);
    }

    @Test
    void createPet_shouldAddVaccinationsWhenValid() throws InvalidBreedException, InvalidVaccinationException {
        Vaccination vaccination = new Vaccination(1L, "Rabies", VaccinationType.ForPuppy, 6);
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breed));
        when(vaccinationRepository.getVaccinationById(1L)).thenReturn(Optional.of(vaccination));
        when(petRepository.createPet(any(Pet.class))).thenReturn(50L);

        Pet newPet = new Pet(null, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());

        List<Long> vaccinationIds = new ArrayList<>();
        vaccinationIds.add(1L);

        long result = petService.createPet(newPet, breed.getId(), vaccinationIds);

        assertEquals(50L, result);
        assertEquals(1, newPet.getVaccinationRecords().size());
        assertEquals("Rabies", newPet.getVaccinationRecords().get(0).getVaccination().getName());
        verify(petRepository, times(1)).createPet(any(Pet.class));
    }
}
