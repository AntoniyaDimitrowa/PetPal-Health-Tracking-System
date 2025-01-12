package com.example.petpal.business.impl;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.business.exception.UnauthorizedDataAccessException;
import com.example.petpal.configuration.security.token.IAccessToken;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VaccinationServiceImpl implements IVaccinationService {

    private final IPetRepository petRepository;
    private final IVaccinationRepository vaccinationRepository;
    private final IUserRepository userRepository;
    private final IAccessToken requestAccessToken;

    @Override
    public List<VaccinationRecord> getVaccinationRecordsByPetId(Long petId) throws InvalidPetException, UnauthorizedDataAccessException {
        if(petRepository.getPet(petId).isEmpty()) {
            throw new InvalidPetException(petId);
        }

        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }

        return vaccinationRepository.getVaccinationRecordsByPetId(petId);
    }
    @Override
    public Long createVaccinationRecord(Long petId, Long vaccinationId, Date date) throws InvalidPetException, InvalidVaccinationException, UnauthorizedDataAccessException {
        Pet pet = petRepository.getPet(petId)
                .orElseThrow(() -> new InvalidPetException(petId));

        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }

        Vaccination vaccination = vaccinationRepository.getVaccinationById(vaccinationId)
                .orElseThrow(() -> new InvalidVaccinationException(vaccinationId));

        VaccinationRecord newVaccinationRecord = VaccinationRecord.builder().vaccination(vaccination).date(date).build();
        return vaccinationRepository.addVaccinationRecordToPet(pet.getId(), newVaccinationRecord);
    }

    public List<Vaccination> getVaccinations() {
        return vaccinationRepository.getAllVaccinations();
    }
}
