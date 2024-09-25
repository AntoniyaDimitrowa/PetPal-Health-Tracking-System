package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.converters.VaccinationConverter;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.entity.PetEntity;

import java.util.ArrayList;
import java.util.Optional;

public class VaccinationServiceImpl implements IVaccinationService {

    private final IPetRepository petRepository;

    public VaccinationServiceImpl(IPetRepository petRepository) {
        this.petRepository = petRepository;
    }
    @Override
    public void addVaccinationRecord(long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        petRepository.addVaccinationToPet(petId, VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord));
    }

    @Override
    public ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) throws InvalidPetException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        return VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(petRepository.getVaccinationRecordsByPetId(petId));
    }
}
