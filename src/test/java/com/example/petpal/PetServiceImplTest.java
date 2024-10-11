package com.example.petpal;

import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
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
        pet = new Pet(1, "Buddy", breed, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
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

        Pet newPet = pet;
        newPet.setId(100L);
        assertThrows(InvalidPetException.class, () -> {
            petService.updatePet(newPet, breed.getId());
        });

        verify(petRepository, times(1)).getPet(100L);
    }

    @Test
    void updatePet_shouldThrowInvalidBreedExceptionWhenBreedDoesNotExist() {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(breedRepository.getBreedById(100L)).thenReturn(Optional.empty());

        assertThrows(InvalidBreedException.class, () -> {
            petService.updatePet(pet, 100L);
        });

        verify(breedRepository, times(1)).getBreedById(100L);
    }

    @Test
    void updatePet_shouldUpdatePetWhenPetAndBreedExist() throws InvalidPetException, InvalidBreedException {
        when(petRepository.getPet(1L)).thenReturn(Optional.of(petEntity));
        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(new BreedEntity())); // Mock breed exists

        Pet newPet = pet;
        newPet.setName("123");
        petService.updatePet(newPet, breed.getId());

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
        MoodEntity moodEntity = new MoodEntity(1L, "Energetic", "");
        Mood mood = new Mood(1L, "Energetic", "");

        BreedEntity breedEntity = BreedEntity.builder()
                .id(1L)
                .name("Labrador")
                .normalMood(moodEntity)
                .description("Labrador description")
                .minimumExercisePerDay(1.5)
                .commonHealthProblems(new ArrayList<>())
                .build();

        when(breedRepository.getBreedById(1L)).thenReturn(Optional.of(breedEntity));

        when(petRepository.createPet(any(PetEntity.class))).thenReturn(50L);

        Pet newPet = new Pet();
        newPet.setId(50L);
        newPet.setName("Buddy");
        newPet.setBreed(new Breed(1L, "Labrador", "Friendly dog", mood, 1.5, new ArrayList<>()));
        newPet.setGender(Gender.Male);
        newPet.setBirthdate(new Date());
        newPet.setWeight(25.5);

        long result = petService.createPet(newPet, breedEntity.getId(), new ArrayList<>());

        verify(petRepository, times(1)).createPet(any(PetEntity.class));

        assertEquals(50L, result);
    }
}
