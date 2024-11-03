package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IBreedRepositoryJPA;
import com.example.petpal.persistence.converters.BreedConverter;
import com.example.petpal.persistence.entity.BreedEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BreedRepositoryImpl implements IBreedRepository {

    private final IBreedRepositoryJPA jpaRepo;

    public BreedRepositoryImpl(IBreedRepositoryJPA jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public List<Breed> getAllBreeds() {
        List<BreedEntity> breeds = jpaRepo.findAll();
        return breeds.stream()
                .map(BreedConverter::convertFromBreedEntityToBreed)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Optional<Breed> getBreedById(Long id) {
        return jpaRepo.findById(id).map(BreedConverter::convertFromBreedEntityToBreed);
    }

    @Override
    public Long createBreed(Breed breed) {
        BreedEntity entity = BreedConverter.convertFromBreedToBreedEntity(breed);
        BreedEntity savedEntity = jpaRepo.save(entity);
        return savedEntity.getId();
    }

    @Override
    public boolean deleteBreed(Long id) {
        if (jpaRepo.existsById(id)) {
            jpaRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Breed updateBreed(Long id, Breed updatedBreed) {
        if (jpaRepo.existsById(id)) {
            BreedEntity entity = BreedConverter.convertFromBreedToBreedEntity(updatedBreed);
            entity.setId(id);  // Ensure the ID remains the same
            BreedEntity updatedEntity = jpaRepo.save(entity);
            return BreedConverter.convertFromBreedEntityToBreed(updatedEntity);
        }
        return null;
    }

    @Override
    public Breed updateHealthProblems(Long breedId, List<String> healthProblems) {
        Optional<BreedEntity> breedOpt = jpaRepo.findById(breedId);
        if (breedOpt.isPresent()) {
            BreedEntity breed = breedOpt.get();
            breed.setCommonHealthProblems(healthProblems);
            jpaRepo.save(breed);
            return BreedConverter.convertFromBreedEntityToBreed(breed);
        }
        return null;
    }
}
