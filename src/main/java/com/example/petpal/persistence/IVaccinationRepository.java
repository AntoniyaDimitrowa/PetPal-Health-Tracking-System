package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.VaccinationEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface IVaccinationRepository {
    Optional<VaccinationEntity> getVaccinationById(long id);

    public ArrayList<VaccinationEntity> getAllVaccinations();
}
