package com.example.petpal.business;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;

import java.util.Date;
import java.util.List;

public interface IVaccinationService {
    List<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException;

    Long createVaccinationRecord(Long petId, Long vaccinationId, Date date) throws InvalidPetException, InvalidVaccinationException;

    public List<Vaccination> getVaccinations();
}
