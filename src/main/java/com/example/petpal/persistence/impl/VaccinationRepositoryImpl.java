package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.IVaccinationRepositoryJPA;
import com.example.petpal.persistence.IVaccinationRecordRepositoryJPA;
import com.example.petpal.persistence.converters.PetConverter;
import com.example.petpal.persistence.converters.VaccinationConverter;
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

    @Autowired
    public VaccinationRepositoryImpl(IVaccinationRepositoryJPA vaccinationRepositoryJPA,
                                     IVaccinationRecordRepositoryJPA vaccinationRecordRepositoryJPA) {
        this.vaccinationRepositoryJPA = vaccinationRepositoryJPA;
        this.vaccinationRecordRepositoryJPA = vaccinationRecordRepositoryJPA;
    }

    @Override
    public Optional<Vaccination> getVaccinationById(long id) {
        Optional<VaccinationEntity> vaccinationEntityOptional = vaccinationRepositoryJPA.findById(id);
        return vaccinationEntityOptional.map(VaccinationConverter::convertFromVaccinationEntityToVaccination);  // Use converter
    }

    @Override
    public ArrayList<Vaccination> getAllVaccinations() {
        List<VaccinationEntity> vaccinationEntities = vaccinationRepositoryJPA.findAll();
        return vaccinationEntities.stream()
                .map(VaccinationConverter::convertFromVaccinationEntityToVaccination)  // Convert to domain objects
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Long addVaccinationRecordToPet(Pet pet, VaccinationRecord vaccinationRecord) {
        VaccinationRecordEntity recordEntity = VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(vaccinationRecord);
        recordEntity.setPet(PetConverter.convertFromPetToPetEntity(pet));

        VaccinationRecordEntity savedRecord = vaccinationRecordRepositoryJPA.save(recordEntity);  // Save the record
        return savedRecord.getId();
    }

    @Override
    public ArrayList<VaccinationRecord> getVaccinationRecordsByPetId(long petId) {
        List<VaccinationRecordEntity> recordEntities = vaccinationRecordRepositoryJPA.findByPetId(petId);

        return recordEntities.stream()
                .map(VaccinationConverter::convertFromVaccinationRecordEntityToVaccinationRecord)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
