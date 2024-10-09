package com.example.petpal.business;

import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.ArrayList;

public interface IHealthService {
    ArrayList<HealthRecord> getHealthRecordsByPetId(long petId) throws InvalidPetException;

    void createHealthRecord(long petId, HealthRecord healthRecord) throws InvalidPetException;

}
