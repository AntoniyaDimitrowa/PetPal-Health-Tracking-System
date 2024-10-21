package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.HealthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface IHealthRecordRepositoryJPA extends JpaRepository<HealthRecordEntity, Long> {
    ArrayList<HealthRecordEntity> findByPetId(Long petId);
}
