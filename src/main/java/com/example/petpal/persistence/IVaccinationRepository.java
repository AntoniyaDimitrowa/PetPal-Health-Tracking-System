package com.example.petpal.persistence;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;

import java.util.List;
import java.util.Optional;


public interface IVaccinationRepository {
    Optional<Vaccination> getVaccinationById(long id);

    List<Vaccination> getAllVaccinations();

    Long addVaccinationRecordToPet(Long petId, VaccinationRecord vaccinationRecord);

    List<VaccinationRecord> getVaccinationRecordsByPetId(long petId);
}
