package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
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
    public ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException {
        Optional<Pet> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        return vaccinationRepository.getVaccinationRecordsByPetId(petId);
    }
    @Override
    public void createVaccinationRecord(Long petId, VaccinationRecord vaccinationRecord) throws InvalidPetException, InvalidVaccinationException {
        Optional<Pet> petOptional = petRepository.getPet(petId);
        if (petOptional.isEmpty()) {
            throw new InvalidPetException(petId);
        }
        Optional<Vaccination> vaccinationOptional = vaccinationRepository.getVaccinationById(vaccinationRecord.getVaccination().getId());
        if (vaccinationOptional.isEmpty()) {
            throw new InvalidVaccinationException(petId);
        }
        vaccinationRepository.addVaccinationRecordToPet(petOptional.get(), vaccinationRecord);
    }

    public ArrayList<Vaccination> getVaccinations() {
        return vaccinationRepository.getAllVaccinations();
    }
}
