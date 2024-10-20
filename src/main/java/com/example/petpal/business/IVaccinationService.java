package com.example.petpal.business;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;

import java.util.ArrayList;

public interface IVaccinationService {
    ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException;

    void createVaccinationRecord(Long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException, InvalidVaccinationException;

    public ArrayList<Vaccination> getVaccinations();
}
