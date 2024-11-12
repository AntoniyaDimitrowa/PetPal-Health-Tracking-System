package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class VaccinationServiceImpl implements IVaccinationService {

    private final IPetRepository petRepository;
    private final IVaccinationRepository vaccinationRepository;

    @Override
    public List<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        return vaccinationRepository.getVaccinationRecordsByPetId(petId);
    }
    @Override
    public Long createVaccinationRecord(Long petId, Long vaccinationId, Date date) throws InvalidPetException, InvalidVaccinationException {
        Pet pet = petRepository.getPet(petId)
                .orElseThrow(() -> new InvalidPetException(petId));

        Vaccination vaccination = vaccinationRepository.getVaccinationById(vaccinationId)
                .orElseThrow(() -> new InvalidVaccinationException(vaccinationId));

        VaccinationRecord newVaccinationRecord = VaccinationRecord.builder().vaccination(vaccination).date(date).build();
        return vaccinationRepository.addVaccinationRecordToPet(pet.getId(), newVaccinationRecord);
    }

    public List<Vaccination> getVaccinations() {
        return vaccinationRepository.getAllVaccinations();
    }
}
