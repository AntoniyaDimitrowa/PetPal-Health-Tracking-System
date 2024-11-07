package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.persistence.entity.PetEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetConverterTest {

    private static final Breed breed = new Breed(1L, "Labrador", "Friendly and outgoing", null, 1.5, new ArrayList<>());
    private static final Pet pet = Pet.builder()
            .id(1L)
            .name("Buddy")
            .breed(breed)
            .gender(Gender.MALE)
            .birthdate(new Date())
            .weight(25.0)
            .image("image_url")
            .vaccinationRecords(List.of(VaccinationRecord.builder().build()))
            .healthRecords(List.of(HealthRecord.builder().build()))
            .build();

    private static final PetEntity petEntity = PetEntity.builder()
            .id(1L)
            .name("Buddy")
            .breed(null)  // Assume conversion from breed is handled elsewhere
            .gender(Gender.MALE)
            .birthdate(new Date())
            .weight(25.0)
            .image("image_url")
            .vaccinationRecords(new ArrayList<>())
            .healthRecords(new ArrayList<>())
            .build();

    @Test
    void convertFromPetToPetEntity_shouldConvertSuccessfully() {
        PetEntity result = PetConverter.convertFromPetToPetEntity(pet);

        assertNotNull(result);
        assertEquals(pet.getId(), result.getId());
        assertEquals(pet.getName(), result.getName());
        assertEquals(pet.getGender(), result.getGender());
        assertEquals(pet.getBirthdate(), result.getBirthdate());
        assertEquals(pet.getWeight(), result.getWeight());
        assertEquals(pet.getImage(), result.getImage());
        assertEquals(pet.getVaccinationRecords().size(), result.getVaccinationRecords().size());
        assertEquals(pet.getHealthRecords().size(), result.getHealthRecords().size());
    }

    @Test
    void convertFromPetToPetEntity_shouldReturnNullForNullPet() {
        assertNull(PetConverter.convertFromPetToPetEntity(null));
    }

    @Test
    void convertFromPetEntityToPet_shouldConvertSuccessfully() {
        Pet result = PetConverter.convertFromPetEntityToPet(petEntity);

        assertNotNull(result);
        assertEquals(petEntity.getId(), result.getId());
        assertEquals(petEntity.getName(), result.getName());
        assertEquals(petEntity.getGender(), result.getGender());
        assertEquals(petEntity.getBirthdate(), result.getBirthdate());
        assertEquals(petEntity.getWeight(), result.getWeight());
        assertEquals(petEntity.getImage(), result.getImage());
        assertEquals(petEntity.getVaccinationRecords().size(), result.getVaccinationRecords().size());
        assertEquals(petEntity.getHealthRecords().size(), result.getHealthRecords().size());
    }

    @Test
    void convertFromPetEntityToPet_shouldReturnNullForNullPetEntity() {
        assertNull(PetConverter.convertFromPetEntityToPet(null));
    }

    @Test
    void convertFromPetsToPetEntities_shouldConvertListSuccessfully() {
        List<Pet> pets = List.of(pet);
        List<PetEntity> result = PetConverter.convertFromPetsToPetEntities(pets);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pet.getId(), result.get(0).getId());
        assertEquals(pet.getName(), result.get(0).getName());
    }

    @Test
    void convertFromPetEntitiesToPets_shouldConvertListSuccessfully() {
        List<PetEntity> entities = List.of(petEntity);
        List<Pet> result = PetConverter.convertFromPetEntitiesToPets(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(petEntity.getId(), result.get(0).getId());
        assertEquals(petEntity.getName(), result.get(0).getName());
    }

    @Test
    void convertFromPetsToPetEntities_shouldReturnEmptyListForNullList() {
        List<PetEntity> result = PetConverter.convertFromPetsToPetEntities(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromPetEntitiesToPets_shouldReturnEmptyListForNullList() {
        List<Pet> result = PetConverter.convertFromPetEntitiesToPets(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
