package com.example.petpal.business;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;

import java.util.Date;
import java.util.List;

public interface IVaccinationService {
    List<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException, UnauthorizedDataAccessException;

    Long createVaccinationRecord(Long petId, Long vaccinationId, Date date) throws InvalidPetException, InvalidVaccinationException, UnauthorizedDataAccessException;

    public List<Vaccination> getVaccinations();
}
