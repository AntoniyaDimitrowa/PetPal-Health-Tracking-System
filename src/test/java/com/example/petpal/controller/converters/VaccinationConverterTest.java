package com.example.petpal.controller.converters;

import com.example.petpal.business.domain.Vaccination;
import com.example.petpal.business.domain.VaccinationRecord;
import com.example.petpal.business.domain.enums.VaccinationType;
import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationRecordDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VaccinationConverterTest {

    private static final Date DATE = new Date();

    private static final VaccinationDTO VACCINATION_DTO = VaccinationDTO.builder()
            .id(1L)
            .name("Rabies")
            .type(VaccinationType.FOR_PUPPY)
            .range(12)
            .build();

    private static final Vaccination VACCINATION = Vaccination.builder()
            .id(1L)
            .name("Rabies")
            .type(VaccinationType.FOR_PUPPY)
            .range(12)
            .build();

    private static final VaccinationRecordDTO VACCINATION_RECORD_DTO = VaccinationRecordDTO.builder()
            .vaccination(VACCINATION_DTO)
            .date(DATE)
            .build();

    private static final VaccinationRecord VACCINATION_RECORD = VaccinationRecord.builder()
            .vaccination(VACCINATION)
            .date(DATE)
            .build();

    @Test
    void convertFromVaccinationDTOtoVaccination_shouldConvertCorrectly() {
        Vaccination vaccination = VaccinationConverter.convertFromVaccinationDTOtoVaccination(VACCINATION_DTO);

        assertNotNull(vaccination);
        assertEquals(VACCINATION_DTO.getId(), vaccination.getId());
        assertEquals(VACCINATION_DTO.getName(), vaccination.getName());
        assertEquals(VACCINATION_DTO.getType(), vaccination.getType());
        assertEquals(VACCINATION_DTO.getRange(), vaccination.getRange());
    }

    @Test
    void convertFromVaccinationDTOtoVaccination_shouldReturnNullForNullDTO() {
        assertNull(VaccinationConverter.convertFromVaccinationDTOtoVaccination(null));
    }

    @Test
    void convertFromVaccinationToVaccinationDTO_shouldConvertCorrectly() {
        VaccinationDTO dto = VaccinationConverter.convertFromVaccinationToVaccinationDTO(VACCINATION);

        assertNotNull(dto);
        assertEquals(VACCINATION.getId(), dto.getId());
        assertEquals(VACCINATION.getName(), dto.getName());
        assertEquals(VACCINATION.getType(), dto.getType());
        assertEquals(VACCINATION.getRange(), dto.getRange());
    }

    @Test
    void convertFromVaccinationToVaccinationDTO_shouldReturnNullForNullVaccination() {
        assertNull(VaccinationConverter.convertFromVaccinationToVaccinationDTO(null));
    }

    @Test
    void convertFromVaccinationRecordDTOtoVaccinationRecord_shouldConvertCorrectly() {
        VaccinationRecord vaccinationRecord = VaccinationConverter.convertFromVaccinationRecordDTOtoVaccinationRecord(VACCINATION_RECORD_DTO);

        assertNotNull(vaccinationRecord);
        assertEquals(VACCINATION_RECORD_DTO.getVaccination().getName(), vaccinationRecord.getVaccination().getName());
        assertEquals(VACCINATION_RECORD_DTO.getVaccination().getType(), vaccinationRecord.getVaccination().getType());
        assertEquals(VACCINATION_RECORD_DTO.getDate(), vaccinationRecord.getDate());
    }

    @Test
    void convertFromVaccinationRecordDTOtoVaccinationRecord_shouldReturnNullForNullDTO() {
        assertNull(VaccinationConverter.convertFromVaccinationRecordDTOtoVaccinationRecord(null));
    }

    @Test
    void convertFromVaccinationRecordToVaccinationRecordDTO_shouldConvertCorrectly() {
        VaccinationRecordDTO dto = VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordDTO(VACCINATION_RECORD);

        assertNotNull(dto);
        assertEquals(VACCINATION_RECORD.getVaccination().getName(), dto.getVaccination().getName());
        assertEquals(VACCINATION_RECORD.getVaccination().getType(), dto.getVaccination().getType());
        assertEquals(VACCINATION_RECORD.getDate(), dto.getDate());
    }

    @Test
    void convertFromVaccinationRecordToVaccinationRecordDTO_shouldReturnNullForNullVaccinationRecord() {
        assertNull(VaccinationConverter.convertFromVaccinationRecordToVaccinationRecordDTO(null));
    }

    @Test
    void convertFromVaccinationRecordsToVaccinationRecordsDTOs_shouldConvertListCorrectly() {
        List<VaccinationRecord> records = List.of(VACCINATION_RECORD);

        List<VaccinationRecordDTO> dtos = VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsDTOs(records);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Rabies", dtos.get(0).getVaccination().getName());
    }

    @Test
    void convertFromVaccinationRecordsToVaccinationRecordsDTOs_shouldReturnEmptyListForNullRecords() {
        List<VaccinationRecordDTO> dtos = VaccinationConverter.convertFromVaccinationRecordsToVaccinationRecordsDTOs(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void convertFromVaccinationRecordDTOsToVaccinationRecords_shouldConvertListCorrectly() {
        List<VaccinationRecordDTO> dtos = List.of(VACCINATION_RECORD_DTO);

        List<VaccinationRecord> records = VaccinationConverter.convertFromVaccinationRecordDTOsToVaccinationRecords(dtos);

        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals("Rabies", records.get(0).getVaccination().getName());
    }

    @Test
    void convertFromVaccinationRecordDTOsToVaccinationRecords_shouldReturnEmptyListForNullDTOs() {
        List<VaccinationRecord> records = VaccinationConverter.convertFromVaccinationRecordDTOsToVaccinationRecords(null);
        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    void convertFromVaccinationsToVaccinationsDTOs_shouldConvertListCorrectly() {
        List<Vaccination> vaccinations = List.of(VACCINATION);

        List<VaccinationDTO> dtos = VaccinationConverter.convertFromVaccinationsToVaccinationsDTOs(vaccinations);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Rabies", dtos.get(0).getName());
    }

    @Test
    void convertFromVaccinationsToVaccinationsDTOs_shouldReturnEmptyListForNullVaccinations() {
        List<VaccinationDTO> dtos = VaccinationConverter.convertFromVaccinationsToVaccinationsDTOs(null);
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
