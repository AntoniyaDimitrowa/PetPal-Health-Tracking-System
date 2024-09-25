package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.converters.HealthRecordConverter;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.persistence.IPetRepository;

import java.util.ArrayList;

public class HealthServiceImpl implements IHealthService {
    private final IPetRepository petRepository;

    public HealthServiceImpl(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    public void addHealthRecord(long petId, HealthRecord healthRecord) {
        petRepository.addHealthRecordToPet(petId, HealthRecordConverter.convertFromHealthRecordToHealthRecordEntity(healthRecord));
    }

    @Override
    public ArrayList<HealthRecord> getHealthRecordsByPetId(long petId) {
        return HealthRecordConverter.convertFromHealthRecordEntitiesToHealthRecords(petRepository.getHealthRecordsByPetId(petId));
    }
}
