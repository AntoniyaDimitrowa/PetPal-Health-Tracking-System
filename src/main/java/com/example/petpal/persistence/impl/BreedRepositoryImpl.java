package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Image;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.entity.BreedEntity;
import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import com.example.petpal.persistence.entity.MoodEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BreedRepositoryImpl implements IBreedRepository {
    private final ArrayList<BreedEntity> breeds = new ArrayList<>();
    private final ArrayList<BreedHealthInfoEntity> breedHealthInfos = new ArrayList<>();
    private static long nextBreedId = 1L;

    public BreedRepositoryImpl() {
        MoodEntity energetic = new MoodEntity(1,"Energetic", new Image());
        MoodEntity calm = new MoodEntity(2, "Calm", new Image());
        MoodEntity protective = new MoodEntity(3, "Protective", new Image());

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
        // Adding health info
        breedHealthInfos.add(new BreedHealthInfoEntity(labrador, 1, 10, 300, 500));
        breedHealthInfos.add(new BreedHealthInfoEntity(goldenRetriever, 1, 10, 350, 600));
    }

    @Override
    public ArrayList<BreedEntity> getAllBreeds() {
        return new ArrayList<>(breeds);
    }

    @Override
    public Optional<BreedEntity> getBreedById(long id) {
        return breeds.stream().filter(breed -> breed.getId() == id).findFirst();
    }

    @Override
    public BreedEntity addBreed(BreedEntity breed) {
        breed.setId(nextBreedId++);
        breeds.add(breed);
        return breed;
    }

    @Override
    public BreedEntity updateBreed(long id, BreedEntity updatedBreed) {
        Optional<BreedEntity> existingBreedOpt = getBreedById(id);
        if (existingBreedOpt.isPresent()) {
            BreedEntity existingBreed = existingBreedOpt.get();
            breeds.remove(existingBreed);
            updatedBreed.setId(id); // Keep the same ID
            breeds.add(updatedBreed);
            return updatedBreed;
        }
        return null;
    }

    @Override
    public boolean deleteBreed(long id) {
        return breeds.removeIf(breed -> breed.getId() == id);
    }

    @Override
    public Optional<BreedHealthInfoEntity> getHealthInfoForBreed(long breedId, int age) {
        return breedHealthInfos.stream()
                .filter(info -> info.getBreed().getId() == breedId &&
                        age >= info.getAgeRangeStart() && age <= info.getAgeRangeEnd())
                .findFirst();
    }

    @Override
    public ArrayList<MoodEntity> getMoodsForBreed(long breedId) {
        return getBreedById(breedId).map(breed -> (ArrayList)List.of(breed.getNormalMood())).orElse(new ArrayList<>());
    }

    @Override
    public BreedEntity updateHealthProblems(long breedId, List<String> healthProblems) {
        Optional<BreedEntity> breedOpt = getBreedById(breedId);
        if (breedOpt.isPresent()) {
            BreedEntity breed = breedOpt.get();
            breed.setCommonHealthProblems(new ArrayList<>(healthProblems));
            return breed;
        }
        return null;
    }
}
