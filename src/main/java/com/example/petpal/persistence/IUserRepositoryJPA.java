package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepositoryJPA extends JpaRepository<UserEntity, Long> {
}
