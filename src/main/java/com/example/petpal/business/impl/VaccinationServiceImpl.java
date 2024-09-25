package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.converters.VaccinationConverter;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.persistence.IPetRepository;

import java.util.ArrayList;

public class VaccinationServiceImpl implements IVaccinationService {

    private final IPetRepository petRepository;

    public VaccinationServiceImpl(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    public void addVaccinationRecord(long petId, VaccinationRecord vaccinationRecord) {
        petRepository.addVaccinationToPet(petId, VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord));
    }

    @Override
    public ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) {
        return VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(petRepository.getVaccinationRecordsByPetId(petId));
    }
}
