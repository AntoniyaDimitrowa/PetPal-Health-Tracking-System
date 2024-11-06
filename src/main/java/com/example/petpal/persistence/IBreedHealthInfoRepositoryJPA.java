package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IBreedHealthInfoRepositoryJPA extends JpaRepository<BreedHealthInfoEntity, Long> {
    Optional<BreedHealthInfoEntity> findByBreedIdAndAgeRangeStartLessThanEqualAndAgeRangeEndGreaterThanEqual(Long breedId, int age, int checkAge);

    List<BreedHealthInfoEntity> findAllByBreedId(Long breedId);
}
