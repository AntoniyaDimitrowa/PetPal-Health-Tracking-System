package com.example.petpal.business.impl;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.domain.HealthRecord;

import java.util.ArrayList;

public class HealthServiceImpl implements IHealthService {
    @Override
    public void addHealthRecord(long petId, HealthRecord healthRecord) {

    }

    @Override
    public ArrayList<HealthRecord> getHealthRecordsByPetId(long petId) {
        return null;
    }
}
