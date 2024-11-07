package com.example.petpal.persistence.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.persistence.entity.VaccinationEntity;
import com.example.petpal.persistence.entity.VaccinationRecordEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VaccinationConverterTest {

    private static final Date DATE = new Date();

    private static final Vaccination VACCINATION = Vaccination.builder()
            .id(1L)
            .name("Rabies")
            .type(VaccinationType.FOR_PUPPY)
            .range(12)
            .build();

    private static final VaccinationEntity VACCINATION_ENTITY = VaccinationEntity.builder()
            .id(1L)
            .name("Rabies")
            .type(VaccinationType.FOR_PUPPY)
            .range(12)
            .build();

    private static final VaccinationRecord VACCINATION_RECORD = VaccinationRecord.builder()
            .vaccination(VACCINATION)
            .date(DATE)
            .build();

    private static final VaccinationRecordEntity VACCINATION_RECORD_ENTITY = VaccinationRecordEntity.builder()
            .id(1L)
            .vaccination(VACCINATION_ENTITY)
            .date(DATE)
            .build();

    @Test
    void convertFromVaccinationEntityToVaccination_shouldConvertCorrectly() {
        Vaccination result = VaccinationConverter.convertFromVaccinationEntityToVaccination(VACCINATION_ENTITY);

        assertNotNull(result);
        assertEquals(VACCINATION_ENTITY.getId(), result.getId());
        assertEquals(VACCINATION_ENTITY.getName(), result.getName());
        assertEquals(VACCINATION_ENTITY.getType(), result.getType());
        assertEquals(VACCINATION_ENTITY.getRange(), result.getRange());
    }

    @Test
    void convertFromVaccinationEntityToVaccination_shouldReturnNullForNullVaccinationEntity() {
        assertNull(VaccinationConverter.convertFromVaccinationEntityToVaccination(null));
    }

    @Test
    void convertFromVaccinationRecordEntityToVaccinationRecord_shouldConvertCorrectly() {
        VaccinationRecord result = VaccinationConverter.convertFromVaccinationRecordEntityToVaccinationRecord(VACCINATION_RECORD_ENTITY);

        assertNotNull(result);
        assertEquals(VACCINATION_RECORD_ENTITY.getId(), result.getId());
        assertEquals(VACCINATION_RECORD_ENTITY.getVaccination().getName(), result.getVaccination().getName());
        assertEquals(VACCINATION_RECORD_ENTITY.getDate(), result.getDate());
    }

    @Test
    void convertFromVaccinationRecordEntityToVaccinationRecord_shouldReturnNullForNullVaccinationRecordEntity() {
        assertNull(VaccinationConverter.convertFromVaccinationRecordEntityToVaccinationRecord(null));
    }

    @Test
    void convertFromVaccinationRecordEntitiesToVaccinationRecords_shouldConvertListCorrectly() {
        List<VaccinationRecordEntity> entities = List.of(VACCINATION_RECORD_ENTITY);

        List<VaccinationRecord> result = VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VACCINATION_RECORD_ENTITY.getVaccination().getName(), result.get(0).getVaccination().getName());
    }

    @Test
    void convertFromVaccinationEntitiesToVaccination_shouldConvertListCorrectly() {
        List<VaccinationEntity> entities = List.of(VACCINATION_ENTITY);

        List<Vaccination> result = VaccinationConverter.convertFromVaccinationEntitiesToVaccination(entities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VACCINATION_ENTITY.getName(), result.get(0).getName());
    }

    @Test
    void convertFromVaccinationRecordToVaccinationRecordEntity_shouldConvertCorrectly() {
        VaccinationRecordEntity result = VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(VACCINATION_RECORD);

        assertNotNull(result);
        assertEquals(VACCINATION_RECORD.getId(), result.getId());
        assertEquals(VACCINATION_RECORD.getVaccination().getName(), result.getVaccination().getName());
        assertEquals(VACCINATION_RECORD.getDate(), result.getDate());
    }

    @Test
    void convertFromVaccinationRecordToVaccinationRecordEntity_shouldReturnNullForNullVaccinationRecord() {
        assertNull(VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordEntity(null));
    }

    @Test
    void convertFromVaccinationToVaccinationEntity_shouldConvertCorrectly() {
        VaccinationEntity result = VaccinationConverter.convertFromVaccinationToVaccinationEntity(VACCINATION);

        assertNotNull(result);
        assertEquals(VACCINATION.getId(), result.getId());
        assertEquals(VACCINATION.getName(), result.getName());
        assertEquals(VACCINATION.getType(), result.getType());
        assertEquals(VACCINATION.getRange(), result.getRange());
    }

    @Test
    void convertFromVaccinationToVaccinationEntity_shouldReturnNullForNullVaccination() {
        assertNull(VaccinationConverter.convertFromVaccinationToVaccinationEntity(null));
    }

    @Test
    void convertFromVaccinationRecordsToVaccinationRecordsEntities_shouldConvertListCorrectly() {
        List<VaccinationRecord> records = List.of(VACCINATION_RECORD);

        List<VaccinationRecordEntity> result = VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsEntities(records);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(VACCINATION_RECORD.getVaccination().getName(), result.get(0).getVaccination().getName());
    }

    @Test
    void convertFromVaccinationRecordsToVaccinationRecordsEntities_shouldReturnEmptyListIfNull() {
        List<VaccinationRecordEntity> result = VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsEntities(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromVaccinationRecordEntitiesToVaccinationRecords_shouldReturnEmptyListIfNull() {
        List<VaccinationRecord> result = VaccinationConverter.convertFromVaccinationRecordEntitiesToVaccinationRecords(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void convertFromVaccinationEntitiesToVaccination_shouldReturnEmptyListIfNull() {
        List<Vaccination> result = VaccinationConverter.convertFromVaccinationEntitiesToVaccination(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
