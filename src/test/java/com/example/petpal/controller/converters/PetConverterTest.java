package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.controller.dto.breed.BreedDTO;
import com.example.petpal.controller.dto.health.HealthRecordDTO;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetConverterTest {

    private static final Breed breed = new Breed(1L, "Labrador", "Friendly and outgoing", null, 1.5, new ArrayList<>());
    private static final BreedDTO breedDTO = new BreedDTO(1L, "Labrador", "Friendly and outgoing", null, 1.5, new ArrayList<>());

    private static final List<VaccinationRecord> vaccinations = List.of(
            VaccinationRecord.builder().build()
    );
    private static final List<HealthRecord> healthRecords = List.of(
            HealthRecord.builder().build()
    );

    private static final Pet pet = Pet.builder()
            .id(1L)
            .name("Buddy")
            .breed(breed)
            .gender(Gender.MALE)
            .birthdate(new Date())
            .weight(25.0)
            .image("image_url")
            .vaccinationRecords(vaccinations)
            .healthRecords(healthRecords)
            .build();

    private static final PetDTO petDTO = PetDTO.builder()
            .id(1L)
            .name("Buddy")
            .breed(breedDTO)
            .gender(Gender.MALE)
            .birthdate(new Date())
            .weight(25.0)
            .image("image_url")
            .vaccinationRecords(List.of(
                    VaccinationRecordDTO.builder().build()
            ))
            .healthRecords(List.of(
                    HealthRecordDTO.builder().build()
            ))
            .build();

    @Test
    void convertFromPetToPetDTO_shouldReturnDTOWhenPetIsValid() {
        PetDTO result = PetConverter.convertFromPetToPetDTO(pet);

        assertNotNull(result);
        assertEquals(pet.getId(), result.getId());
        assertEquals(pet.getName(), result.getName());
        assertEquals(pet.getBreed().getId(), result.getBreed().getId());
        assertEquals(pet.getGender(), result.getGender());
        assertEquals(pet.getBirthdate(), result.getBirthdate());
        assertEquals(pet.getWeight(), result.getWeight());
        assertEquals(pet.getImage(), result.getImage());
        assertEquals(pet.getVaccinationRecords().size(), result.getVaccinationRecords().size());
        assertEquals(pet.getHealthRecords().size(), result.getHealthRecords().size());
    }

    @Test
    void convertFromPetToPetDTO_shouldReturnNullWhenPetIsNull() {
        assertNull(PetConverter.convertFromPetToPetDTO(null));
    }

    @Test
    void convertFromPetDTOToPet_shouldReturnPetWhenDTOIsValid() {
        Pet result = PetConverter.convertFromPetDTOToPet(petDTO);

        assertNotNull(result);
        assertEquals(petDTO.getId(), result.getId());
        assertEquals(petDTO.getName(), result.getName());
        assertEquals(petDTO.getBreed().getId(), result.getBreed().getId());
        assertEquals(petDTO.getGender(), result.getGender());
        assertEquals(petDTO.getBirthdate(), result.getBirthdate());
        assertEquals(petDTO.getWeight(), result.getWeight());
        assertEquals(petDTO.getImage(), result.getImage());
        assertEquals(petDTO.getVaccinationRecords().size(), result.getVaccinationRecords().size());
        assertEquals(petDTO.getHealthRecords().size(), result.getHealthRecords().size());
    }

    @Test
    void convertFromPetDTOToPet_shouldReturnNullWhenDTOIsNull() {
        assertNull(PetConverter.convertFromPetDTOToPet(null));
    }

    @Test
    void convertFromCreatePetDTOToPet_shouldReturnPetWhenDTOIsValid() {
        CreatePetDTO createPetDTO = CreatePetDTO.builder()
                .name("Buddy")
                .gender(Gender.MALE)
                .birthdate(new Date())
                .weight(25.0)
                .image("image_url")
                .build();

        Pet result = PetConverter.convertFromCreatePetDTOToPet(createPetDTO);

        assertNotNull(result);
        assertEquals(createPetDTO.getName(), result.getName());
        assertEquals(createPetDTO.getGender(), result.getGender());
        assertEquals(createPetDTO.getBirthdate(), result.getBirthdate());
        assertEquals(createPetDTO.getWeight(), result.getWeight());
        assertEquals(createPetDTO.getImage(), result.getImage());
    }

    @Test
    void convertFromCreatePetDTOToPet_shouldReturnNullWhenDTOIsNull() {
        assertNull(PetConverter.convertFromCreatePetDTOToPet(null));
    }

    @Test
    void convertFromPetsToPetDTOs_shouldReturnDTOListWhenPetsAreValid() {
        Pet pet2 = pet;
        pet2.setName("Milo");
        List<Pet> pets = List.of(pet, pet2);

        List<PetDTO> result = PetConverter.convertFromPetsToPetDTOs(pets);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(pets.get(0).getName(), result.get(0).getName());
        assertEquals(pets.get(1).getName(), result.get(1).getName());
    }

    @Test
    void convertFromPetsToPetDTOs_shouldReturnEmptyListWhenPetsListIsNull() {
        List<PetDTO> result = PetConverter.convertFromPetsToPetDTOs(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromPetDTOsToPets_shouldReturnPetListWhenDTOsAreValid() {
        PetDTO petDTO2 = petDTO;
        petDTO2.setName("Milo");
        List<PetDTO> petDTOs = List.of(petDTO, petDTO2);

        List<Pet> result = PetConverter.convertFromPetDTOsToPets(petDTOs);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(petDTOs.get(0).getName(), result.get(0).getName());
        assertEquals(petDTOs.get(1).getName(), result.get(1).getName());
    }

    @Test
    void convertFromPetDTOsToPets_shouldReturnEmptyListWhenDTOsListIsNull() {
        List<Pet> result = PetConverter.convertFromPetDTOsToPets(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
