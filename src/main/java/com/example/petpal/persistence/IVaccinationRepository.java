package com.example.petpal.persistence;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;

import java.util.ArrayList;
import java.util.Optional;


public interface IVaccinationRepository {
    Optional<Vaccination> getVaccinationById(long id);

    public ArrayList<Vaccination> getAllVaccinations();

    Long addVaccinationRecordToPet(Pet pet, VaccinationRecord vaccinationRecord);

    ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId);
}
