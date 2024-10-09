package com.example.petpal.persistence.impl;

import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.entity.VaccinationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VaccinationRepositoryImpl implements IVaccinationRepository {
    public VaccinationRepositoryImpl(){}
    @Override
    public Optional<VaccinationEntity> getVaccinationById(long id) {
        return Optional.empty();
    }
}
