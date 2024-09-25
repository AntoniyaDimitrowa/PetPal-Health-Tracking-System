package com.example.petpal.business;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.VaccinationRecord;

import java.util.ArrayList;

public interface IHealthService {
    void addHealthRecord(long petId, HealthRecord healthRecord);

    ArrayList<HealthRecord> getHealthRecordsByPetId(long petId);
}
