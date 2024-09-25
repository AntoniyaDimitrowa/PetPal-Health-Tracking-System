package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.converters.HealthConverter;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.PetEntity;

import java.util.ArrayList;
import java.util.Optional;

public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;

    public HealthServiceImpl(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    public void addHealthRecord(long petId, HealthRecord healthRecord) throws InvalidPetException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        petRepository.addHealthRecordToPet(petId, HealthConverter.convertFromHealthRecordToHealthRecordEntity(healthRecord));
    }

    @Override
    public ArrayList<HealthRecord> getHealthRecordsByPetId(long petId) throws InvalidPetException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        return HealthConverter.convertFromHealthRecordEntitiesToHealthRecords(petRepository.getHealthRecordsByPetId(petId));
    }
}
