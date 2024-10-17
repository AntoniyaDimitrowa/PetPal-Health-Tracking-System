package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.persistence.IVaccinationRepository;
import com.example.petpal.persistence.entity.VaccinationEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VaccinationRepositoryImpl implements IVaccinationRepository {

    private List<VaccinationEntity> vaccinations = new ArrayList<>();

    // Constructor to populate the list
    public VaccinationRepositoryImpl() {
        vaccinations.add(VaccinationEntity.builder()
                .id(1L)
                .name("Distemper")
                .type(VaccinationType.ForPuppy)
                .range(6) // 6-8 weeks
                .build());

        vaccinations.add(VaccinationEntity.builder()
                .id(2L)
                .name("Parvovirus")
                .type(VaccinationType.ForPuppy)
                .range(6) // 6-8 weeks
                .build());

        vaccinations.add(VaccinationEntity.builder()
                .id(3L)
                .name("Adenovirus")
                .type(VaccinationType.ForPuppy)
                .range(10) // 10-12 weeks
                .build());

        vaccinations.add(VaccinationEntity.builder()
                .id(4L)
                .name("Rabies")
                .type(VaccinationType.ForPuppy)
                .range(12) // 12-16 weeks
                .build());
    }

    @Override
    public Optional<VaccinationEntity> getVaccinationById(long id) {
        return vaccinations.stream()
                .filter(vaccination -> vaccination.getId() == id)
                .findFirst();
    }

    public ArrayList<VaccinationEntity> getAllVaccinations() {
        return new ArrayList<>(vaccinations);
    }
}
