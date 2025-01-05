package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.HealthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHealthRecordRepositoryJPA extends JpaRepository<HealthRecordEntity, Long> {
    List<HealthRecordEntity> findByPetId(Long petId);

    @Query(value = """
        SELECT hr.date, hr.food_intake, hr.water_intake, hr.activity_level, 
               bhi.normal_food_intake, bhi.normal_water_intake, b.minimum_exercise_per_day
        FROM health_record hr
        JOIN pet p ON hr.pet_id = p.id
        JOIN breed b ON p.breed_id = b.id
        JOIN breed_health_info bhi ON bhi.breed_id = b.id
        WHERE hr.pet_id = :petId 
            AND MONTH(hr.date) = :month 
            AND YEAR(hr.date) = :year
            AND TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()) BETWEEN bhi.age_range_start AND bhi.age_range_end
    """, nativeQuery = true)
    List<Object[]> findHealthRecordsWithNormsForPet(
            @Param("petId") Long petId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT m.id, m.name, COUNT(hr.mood.id) AS moodCount
        FROM HealthRecordEntity hr
        JOIN MoodEntity m ON hr.mood.id = m.id
        WHERE hr.pet.id = :petId 
          AND MONTH(hr.date) = :month 
          AND YEAR(hr.date) = :year
        GROUP BY m.id, m.name
    """)
    List<Object[]> findMoodDistributionForPet(
            @Param("petId") Long petId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query(value = """
    SELECT hr.*
    FROM health_record hr
    WHERE hr.pet_id = :petId
    ORDER BY hr.date DESC
    LIMIT :numberOfRecords
""", nativeQuery = true)
    List<HealthRecordEntity> findRecentRecordsByPetId(
            @Param("petId") Long petId,
            @Param("numberOfRecords") int numberOfRecords
    );}
