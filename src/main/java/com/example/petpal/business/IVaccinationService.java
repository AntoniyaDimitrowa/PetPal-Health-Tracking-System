package com.example.petpal.business;

import com.example.petpal.business.domain.VaccinationRecord;

import java.util.ArrayList;

public interface IVaccinationService {
    void addVaccinationRecord(long petId, VaccinationRecord vaccinationRecord);

    ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId);
}
