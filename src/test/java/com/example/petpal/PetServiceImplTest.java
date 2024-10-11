package com.example.petpal;

import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.impl.PetServiceImpl;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.MoodEntity;
import com.example.petpal.persistence.entity.PetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @Mock
    private IPetRepository petRepository;

    @Mock
    private IBreedRepository breedRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private PetEntity petEntity;
    private Pet pet;
    private Breed breed;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        MoodEntity energetic = new MoodEntity(1,"Energetic", "");

        BreedEntity breedEntity = BreedEntity.builder()
                .id(1L)
                .name("Labrador")
                .description("Labradors are friendly, outgoing, and high-spirited companions.")
                .normalMood(energetic)
                .minimumExercisePerDay(1.5)  // 1.5 hours
                .commonHealthProblems(new ArrayList<>(Arrays.asList("Hip dysplasia", "Obesity")))
                .build();

        breed = BreedConverter.convertFromBreedEntityToBreed(breedEntity);
        petEntity = new PetEntity(1, "Buddy", breedEntity, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
        pet = new Pet(1, "Buddy", breed, Gender.Male, new Date(), 25.5, new byte[]{}, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void getPet_shouldReturnPetWhenPetExists() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));

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

        assertThrows(InvalidPetException.class, () -> {
            petService.updatePet(100L, "Buddy", breed.getId(), Gender.Male, new Date(), 10.0);
        });

        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidBreedExceptionWhenBreedDoesNotExist() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        Breed invalidBreed = Breed.builder().id(100L).build();
        assertThrows(InvalidBreedException.class, () -> {
            petService.updatePet(1L, "Buddy", breed.getId(), Gender.Male, new Date(), 12.0);
        });

        verify(breedRepository, times(1)).getBreedById(1L);
    }

    @Test
    void updatePet_shouldUpdatePetWhenPetAndBreedExist() throws InvalidPetException, InvalidBreedException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(new BreedEntity())); // Mock breed exists

        petService.updatePet(1L, "Buddy", breed.getId(), Gender.Male, new Date(), 12.0);

        verify(petRepository, times(1)).updatePet(eq(1L), any(PetEntity.class));
        verify(breedRepository, times(1)).getBreedById(1L); // Verify breed lookup
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        petService.deletePet(1L);

        verify(petRepository, times(1)).deletePet(1L);
    }

    @Test
    void createPet_shouldReturnCreatedPet() throws InvalidBreedException {
        when(petRepository.createPet(any(PetEntity.class))).thenReturn(petEntity);
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(new BreedEntity()));

        Pet result = petService.createPet("Buddy", breed.getId(), Gender.Male, new Date(), 25.5, new ArrayList<>());

        assertNotNull(result);
        assertEquals(pet, result);
        verify(petRepository, times(1)).createPet(any(PetEntity.class));
    }
}
