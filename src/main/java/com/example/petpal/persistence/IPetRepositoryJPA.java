package com.example.petpal.persistence;


import com.example.petpal.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPetRepositoryJPA extends JpaRepository<PetEntity, Long> {

}
