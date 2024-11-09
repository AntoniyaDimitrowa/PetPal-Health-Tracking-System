package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.persistence.*;
import com.example.petpal.persistence.converters.VaccinationConverter;
import com.example.petpal.persistence.entity.PetEntity;
import com.example.petpal.persistence.entity.VaccinationEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VaccinationRepositoryImpl implements IVaccinationRepository {

    private final IVaccinationRepositoryJPA vaccinationRepositoryJPA;
    private final IVaccinationRecordRepositoryJPA vaccinationRecordRepositoryJPA;
    private final IPetRepositoryJPA petRepositoryJPA;

    @Autowired
    public VaccinationRepositoryImpl(IVaccinationRepositoryJPA vaccinationRepositoryJPA,
                                     IVaccinationRecordRepositoryJPA vaccinationRecordRepositoryJPA, IPetRepositoryJPA petRepository) {
        this.vaccinationRepositoryJPA = vaccinationRepositoryJPA;
        this.vaccinationRecordRepositoryJPA = vaccinationRecordRepositoryJPA;
        this.petRepositoryJPA = petRepository;
    }

    @Override
    public Optional<Vaccination> getVaccinationById(long id) {
        Optional<VaccinationEntity> vaccinationEntityOptional = vaccinationRepositoryJPA.findById(id);
        return vaccinationEntityOptional.map(VaccinationConverter::convertFromVaccinationEntityToVaccination);  // Use converter
    }

    @Override
    public List<Vaccination> getAllVaccinations() {
        List<VaccinationEntity> vaccinationEntities = vaccinationRepositoryJPA.findAll();
        return vaccinationEntities.stream()
                .map(VaccinationConverter::convertFromVaccinationEntityToVaccination)  // Convert to domain objects
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Long addVaccinationRecordToPet(Long petId, VaccinationRecord vaccinationRecord) {
        PetEntity pet = petRepositoryJPA.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        VaccinationRecordEntity recordEntity = VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord);
        recordEntity.setPet(pet);

        VaccinationRecordEntity savedRecord = vaccinationRecordRepositoryJPA.save(recordEntity);  // Save the record
        return savedRecord.getId();
    }



    @Override
    public List<VaccinationRecord> getVaccinationRecordsByPetId(long petId) {
        List<VaccinationRecordEntity> recordEntities = vaccinationRecordRepositoryJPA.findByPetId(petId);

        return recordEntities.stream()
                .map(VaccinationConverter::convertFromVaccinationRecordEntityToVaccinationRecord)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
