package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.persistence.converters.VaccinationConverter;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.VaccinationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VaccinationServiceImpl implements IVaccinationService {

    private final IPetRepository petRepository;
    private final IVaccinationRepository vaccinationRepository;

    @Override
    public ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) throws InvalidPetException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        return VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(petRepository.getVaccinationRecordsByPetId(petId));
    }
    @Override
    public void createVaccinationRecord(long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException, InvalidVaccinationException {
        Optional<PetEntity> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        Optional<VaccinationEntity> vaccinationOptional = vaccinationRepository.getVaccinationById(vaccinationRecord.getVaccination().getId());
        if (vaccinationOptional.isEmpty()) {
            throw new InvalidVaccinationException(petId);
        }
        petRepository.addVaccinationToPet(petId, VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord));
    }

    public ArrayList<Vaccination> getVaccinations() {
        return VaccinationConverter.convertFromVaccinationEntitiesToVaccination(vaccinationRepository.getAllVaccinations());
    }
}
