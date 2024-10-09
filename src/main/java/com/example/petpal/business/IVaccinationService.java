package com.example.petpal.business;

import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;

import java.util.ArrayList;

public interface IVaccinationService {
    ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) throws InvalidPetException;

    void createVaccinationRecord(long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException, InvalidVaccinationException;

}
