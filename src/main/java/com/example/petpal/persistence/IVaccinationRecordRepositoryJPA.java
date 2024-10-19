package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.VaccinationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVaccinationRecordRepositoryJPA extends JpaRepository<VaccinationRecordEntity, Long> {
    List<VaccinationRecordEntity> findByPetId(Long petId);
}
