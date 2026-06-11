package com.example.petpal.business.impl;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.*;
import com.example.petpal.business.exception.*;
import com.example.petpal.configuration.security.token.IAccessToken;
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
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetServiceImpl implements IPetService {
    private final IPetRepository petRepository;
    private final IBreedRepository breedRepository;
    private final IVaccinationRepository vaccinationRepository;
    private final IUserRepository userRepository;
    private final IAccessToken requestAccessToken;

    @Override
    public Optional<Pet> getPet(Long petId) throws UnauthorizedDataAccessException {
        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }

        return petRepository.getPet(petId);
    }

    @Transactional
    @Override
    public Long createPet(Pet pet, Long breedId, List<Long> vaccinationRecordsIds, Long userId) throws InvalidVaccinationException, InvalidBreedException, InvalidUserException, CreationFailException, UnauthorizedDataAccessException {
        try {
            User user = userRepository.getUserById(userId)
                    .orElseThrow(() -> new InvalidUserException(userId));
            if(!Objects.equals(requestAccessToken.getUserId(), user.getId())) {
                throw new UnauthorizedDataAccessException();
            }
            if(1 == 2) { //dummy check
                throw new UnauthorizedDataAccessException();
            }
//            Long authenticatedUserId = requestAccessToken.getUserId();

//            User user = userRepository.getUserById(authenticatedUserId)
//                    .orElseThrow(() -> new InvalidUserException(authenticatedUserId));

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
        } catch (InvalidUserException | InvalidBreedException | InvalidVaccinationException | UnauthorizedDataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new CreationFailException("Failed to create pet and associated vaccination records." + e.getMessage());
        }
    }

    @Override
    public void updatePet(Pet pet, Long breedId) throws InvalidPetException, InvalidBreedException, UnauthorizedDataAccessException {
        Pet existingPet = petRepository.getPet(pet.getId())
                .orElseThrow(() -> new InvalidPetException(pet.getId()));

        Optional<User> owner = userRepository.getUserByPetId(pet.getId());
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }
        Breed breed = breedRepository.getBreedById(breedId)
                .orElseThrow(() -> new InvalidBreedException(breedId));

        existingPet.setName(pet.getName());
        existingPet.setBreed(breed);
        existingPet.setBirthdate(pet.getBirthdate());
        existingPet.setImage(pet.getImage());
        existingPet.setGender(pet.getGender());
        existingPet.setWeight(pet.getWeight());

        petRepository.updatePet(existingPet.getId(), existingPet);
    }

    @Override
    public boolean deletePet(Long petId) throws UnauthorizedDataAccessException {
        Optional<User> owner = userRepository.getUserByPetId(petId);
        if(owner.isEmpty() || !Objects.equals(requestAccessToken.getUserId(), owner.get().getId())) {
            throw new UnauthorizedDataAccessException();
        }

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
