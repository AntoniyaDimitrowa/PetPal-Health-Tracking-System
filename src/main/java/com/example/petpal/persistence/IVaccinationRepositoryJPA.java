package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.VaccinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVaccinationRepositoryJPA extends JpaRepository<VaccinationEntity, Long> {
}
