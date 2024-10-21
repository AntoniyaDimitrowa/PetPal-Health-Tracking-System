package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMoodRepositoryJPA extends JpaRepository<MoodEntity, Long> {
}
