package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.BreedHealthInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IBreedHealthInfoRepositoryJPA extends JpaRepository<BreedHealthInfoEntity, Long> {
    @Query("""
    SELECT bhi 
    FROM BreedHealthInfoEntity bhi
    WHERE bhi.breed.id = :breedId
      AND :age BETWEEN bhi.ageRangeStart AND bhi.ageRangeEnd
""")
    Optional<BreedHealthInfoEntity> findByBreedIdAndAgeRangeStartLessThanEqualAndAgeRangeEndGreaterThanEqual(
            @Param("breedId") Long breedId,
            @Param("age") int age
    );

    List<BreedHealthInfoEntity> findAllByBreedId(Long breedId);
}
