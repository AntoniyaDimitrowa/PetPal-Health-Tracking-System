package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepositoryJPA extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN PetEntity p ON u.id = p.owner.id WHERE p.id = :petId")
    Optional<UserEntity> findByPetId(@Param("petId") Long petId);
}
