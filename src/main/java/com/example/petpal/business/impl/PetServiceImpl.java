package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.*;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IPetRepository;
import com.example.petpal.persistence.IUserRepository;
import com.example.petpal.persistence.IVaccinationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetServiceImpl implements IPetService {
    private final IPetRepository petRepository;
    private final IBreedRepository breedRepository;
    private final IVaccinationRepository vaccinationRepository;
    private final IUserRepository userRepository;

    @Override
    public Optional<Pet> getPet(Long petId) {
        return petRepository.getPet(petId);
    }

    @Transactional
    @Override
    public Long createPet(Pet pet, Long breedId, List<Long> vaccinationRecordsIds, Long userId) throws InvalidVaccinationException, InvalidBreedException, InvalidUserException, CreationFailException {
        try {
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new InvalidUserException(userId));

            Breed breed = breedRepository.getBreedById(breedId)
                    .orElseThrow(() -> new InvalidBreedException(breedId));

            pet.setBreed(breed);

            // Create the pet
            long petId = petRepository.createPet(pet, user);

            // Create vaccination records
            for (Long vaccinationId : vaccinationRecordsIds) {
                Vaccination vaccination = vaccinationRepository.getVaccinationById(vaccinationId)
                        .orElseThrow(() -> new InvalidVaccinationException(vaccinationId));

                VaccinationRecord vaccinationRecord = VaccinationRecord.builder()
                        .vaccination(vaccination)
                        .date(calculateInitialVaccinationRecordDate(pet.getBirthdate(), vaccination.getRange()))
                        .build();

                vaccinationRepository.addVaccinationRecordToPet(petId, vaccinationRecord);
            }

            return petId;
        } catch (InvalidUserException | InvalidBreedException | InvalidVaccinationException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationFailException("Failed to create pet and associated vaccination records." + e.getMessage());
        }
    }

    @Override
    public void updatePet(Pet pet, Long breedId) throws InvalidPetException, InvalidBreedException {
        Pet existingPet = petRepository.getPet(pet.getId())
                .orElseThrow(() -> new InvalidPetException(pet.getId()));

        Breed breed = breedRepository.getBreedById(breedId)
                .orElseThrow(() -> new InvalidBreedException(breedId));

        pet.setBreed(breed);

        petRepository.updatePet(existingPet.getId(), pet);
    }

    @Override
    public boolean deletePet(Long petId) {
        return petRepository.deletePet(petId);
    }

    private Date calculateInitialVaccinationRecordDate(Date petBirthDate, int weeks) {
        LocalDate localDate = petBirthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate updatedDate = localDate.plusWeeks(weeks);

        return Date.from(updatedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
