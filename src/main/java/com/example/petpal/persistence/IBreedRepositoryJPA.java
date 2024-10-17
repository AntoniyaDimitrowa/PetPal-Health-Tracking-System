package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.BreedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBreedRepositoryJPA extends JpaRepository<BreedEntity, Long> {
}
