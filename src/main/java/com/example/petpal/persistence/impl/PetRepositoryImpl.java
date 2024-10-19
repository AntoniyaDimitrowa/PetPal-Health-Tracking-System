package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PetRepositoryImpl implements IPetRepository {
    private final ArrayList<PetEntity> pets = new ArrayList<>();
    private final ArrayList<BreedEntity> breeds = new ArrayList<>();
    private static long nextId = 1;

    public PetRepositoryImpl() {
        // Initialize some breed entities
        MoodEntity energetic = new MoodEntity(1L,"Energetic", "");
        MoodEntity calm = new MoodEntity(2L, "Calm", "");
        MoodEntity protective = new MoodEntity(3L, "Protective", "");

        // Initialize some breed entities
        BreedEntity labrador = BreedEntity.builder()
                .id(1L)
                .name("Labrador")
                .description("Labradors are friendly, outgoing, and high-spirited companions.")
                .normalMood(energetic)
                .minimumExercisePerDay(1.5)  // 1.5 hours
                .commonHealthProblems(new ArrayList<>(Arrays.asList("Hip dysplasia", "Obesity")))
                .build();

        BreedEntity goldenRetriever = BreedEntity.builder()
                .id(2L)
                .name("Golden Retriever")
                .description("Golden Retrievers are intelligent, friendly, and devoted dogs.")
                .normalMood(calm)
                .minimumExercisePerDay(1.0)  // 1 hour
                .commonHealthProblems(new ArrayList<>(Arrays.asList("Elbow dysplasia", "Heart problems")))
                .build();

        BreedEntity bulldog = BreedEntity.builder()
                .id(3L)
                .name("Bulldog")
                .description("Bulldogs are calm, courageous, and friendly.")
                .normalMood(protective)
                .minimumExercisePerDay(0.5)  // 30 minutes
                .commonHealthProblems(new ArrayList<>(Arrays.asList("Breathing problems", "Skin infections")))
                .build();

        breeds.addAll(Arrays.asList(labrador, goldenRetriever, bulldog));

        // Initialize some pet entities
        PetEntity pet1 = new PetEntity(nextId++, "Buddy", labrador, Gender.Male, new Date(), 25.5, "", new ArrayList<>(), new ArrayList<>());
        PetEntity pet2 = new PetEntity(nextId++, "Bella", goldenRetriever, Gender.Female, new Date(), 22.3, "", new ArrayList<>(), new ArrayList<>());
        PetEntity pet3 = new PetEntity(nextId++, "Charlie", bulldog, Gender.Male, new Date(), 30.0, "", new ArrayList<>(), new ArrayList<>());

        pets.addAll(Arrays.asList(pet1, pet2, pet3));
    }

    @Override
    public Optional<PetEntity> getPet(long id) {
        return pets.stream().filter(pet -> pet.getId() == id).findFirst();
    }

    @Override
    public void updatePet(long id, PetEntity pet) {
        PetEntity entity = getPet(id).get();
        entity.setName(pet.getName());
        entity.setBreed(pet.getBreed());
        entity.setGender(pet.getGender());
        entity.setBirthdate(pet.getBirthdate());
        entity.setWeight(pet.getWeight());
    }

    @Override
    public boolean deletePet(long petId) {
        return pets.removeIf(pet -> pet.getId() == petId);
    }

    @Override
    public Long createPet(PetEntity pet) {
        if (pet.getId() == 0) {
            pet.setId(nextId++);
            pets.add(pet);
        } else {
            pets.removeIf(p -> p.getId() == pet.getId());
            pets.add(pet);
        }
        return pet.getId();
    }

    @Override
    public void addHealthRecordToPet(long petId, HealthRecordEntity healthRecord) {
        PetEntity entity = getPet(petId).get();
        entity.getHealthRecords().add(healthRecord);
    }

    @Override
    public ArrayList<HealthRecordEntity> getHealthRecordsByPetId(long petId) {
        return getPet(petId).get().getHealthRecords();
    }
}
