package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.HealthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHealthRecordRepositoryJPA extends JpaRepository<HealthRecordEntity, Long> {
    List<HealthRecordEntity> findByPetId(Long petId);
}
