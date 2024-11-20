package com.example.petpal.controller;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidBreedException; // Import the exception
import com.example.petpal.controller.converters.HealthConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.health.BreedHealthInfoDTO;
import com.example.petpal.controller.dto.health.CreateHealthRecordDTO;
import com.example.petpal.controller.dto.health.HealthRecordDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/health")
@AllArgsConstructor
public class HealthController {

    private final IHealthService healthService;

    @GetMapping("/pets/{petId}/records")
    public ResponseEntity<List<HealthRecordDTO>> getHealthRecordsByPetId(@PathVariable Long petId) {
        try {
            List<HealthRecord> healthRecords = healthService.getHealthRecordsByPetId(petId);
            return ResponseEntity.ok(HealthConverter.convertFromHealthRecordsToHealthRecordDTOs(healthRecords));
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pets/{petId}/records")
    public ResponseEntity<CreateEntityResponse> createHealthRecord(@PathVariable Long petId, @RequestBody CreateHealthRecordDTO healthRecordDTO) {
        try {
            HealthRecord healthRecord = HealthConverter.convertFromCreateHealthRecordDTOToHealthRecord(healthRecordDTO);
            Long newHealthRecordId = healthService.createHealthRecord(petId, healthRecord, healthRecordDTO.getMoodId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newHealthRecordId).build());
        } catch (InvalidPetException | InvalidMoodException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/breeds/{breedId}/health")
    public ResponseEntity<List<BreedHealthInfoDTO>> getHealthInfoForBreed(@PathVariable Long breedId) {
        List<BreedHealthInfo> healthInfos = healthService.getHealthInfoByBreedId(breedId);
        return ResponseEntity.ok(HealthConverter.convertFromBreedHealthInfosToDTOs(healthInfos));
    }

    @PostMapping("/breeds/{breedId}/health")
    public ResponseEntity<Void> createHealthInfoForBreed(@PathVariable Long breedId, @RequestBody BreedHealthInfoDTO breedHealthInfoDTO) {
        try {
            BreedHealthInfo breedHealthInfo = HealthConverter.convertFromBreedHealthInfoDTOToBreedHealthInfo(breedHealthInfoDTO);
            healthService.createHealthInfoForBreed(breedId, breedHealthInfo.getAgeRangeStart(), breedHealthInfo.getAgeRangeEnd(), breedHealthInfo);
            return ResponseEntity.status(201).build();
        } catch (InvalidBreedException e) {
            return ResponseEntity.badRequest().build();  // Return a 400 Bad Request if the breed is invalid
        }
    }
}
