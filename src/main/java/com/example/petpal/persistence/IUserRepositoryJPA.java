package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepositoryJPA extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
