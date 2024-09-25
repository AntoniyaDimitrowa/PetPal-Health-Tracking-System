package com.example.petpal.business;

import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;

import java.util.ArrayList;

public interface IVaccinationService {
    void addVaccinationRecord(long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException;

    ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) throws InvalidPetException;
}
