package com.example.petpal.persistence;



import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public interface IPetRepository {
    Optional<PetEntity> getPet(long petId);
    void updatePet(long id, PetEntity pet);
    boolean deletePet(long petId);
    Long createPet(PetEntity pet);
    void addVaccinationToPet(long petId, VaccinationRecordEntity vaccinationRecord);
    ArrayList<VaccinationRecordEntity> getVaccinationRecordsByPetId(long petId);

    void addHealthRecordToPet(long petId, HealthRecordEntity healthRecord);
    ArrayList<HealthRecordEntity> getHealthRecordsByPetId(long petId);
}
