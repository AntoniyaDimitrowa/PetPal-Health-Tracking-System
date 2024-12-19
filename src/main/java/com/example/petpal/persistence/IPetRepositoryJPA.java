package com.example.petpal.persistence;


import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IPetRepositoryJPA extends JpaRepository<PetEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM PetEntity p WHERE p.id = :id")
    void deletePetById(@Param("id") Long id);
}
