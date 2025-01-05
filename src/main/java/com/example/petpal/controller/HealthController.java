package com.example.petpal.controller;

import com.example.petpal.business.IHealthService;
import com.example.petpal.business.IPetHealthAnalyzer;
import com.example.petpal.business.domain.BreedHealthInfo;
import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidBreedException; // Import the exception
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.controller.converters.HealthConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.health.*;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/health")
@AllArgsConstructor
public class HealthController {

    private final IHealthService healthService;
    private final IPetHealthAnalyzer petHealthAnalyzer;
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/pets/{petId}/records")
    @RolesAllowed("Owner")
    public ResponseEntity<List<HealthRecordDTO>> getHealthRecordsByPetId(@PathVariable Long petId) {
        try {
            List<HealthRecord> healthRecords = healthService.getHealthRecordsByPetId(petId);
            return ResponseEntity.ok(HealthConverter.convertFromHealthRecordsToHealthRecordDTOs(healthRecords));
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/pets/{petId}/records")
    @RolesAllowed("Owner")
    public ResponseEntity<CreateEntityResponse> createHealthRecord(@PathVariable Long petId, @RequestBody CreateHealthRecordDTO healthRecordDTO) {
        try {
            HealthRecord healthRecord = HealthConverter.convertFromCreateHealthRecordDTOToHealthRecord(healthRecordDTO);
            Long newHealthRecordId = healthService.createHealthRecord(petId, healthRecord, healthRecordDTO.getMoodId());
            // Execute health analysis asynchronously
            CompletableFuture.runAsync(() -> {
                try {
                    HealthAnalysisResult analysisResult = petHealthAnalyzer.analyzeHealthRecord(petId, healthRecord);
                    log.info("ANALYSIS_RESULT: {}", analysisResult);

                    //TODO WebSockets
                } catch (InvalidPetException | InvalidUserException e) {
                    log.error("Error analyzing health record: {}", e.getMessage(), e);
                }
                catch (Exception e) {
                    log.error("Unexpected error: {}", e.getMessage(), e);
                }

            });
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newHealthRecordId).build());
        } catch (InvalidPetException | InvalidMoodException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pets/{petId}/statistics")
    @RolesAllowed("Owner")
    public ResponseEntity<PetStatisticsDTO> getStatisticsForPet(@PathVariable Long petId,
                                                                @RequestParam(required = false) Integer month,
                                                                @RequestParam(required = false) Integer year) {
        LocalDate now = LocalDate.now();
        int resolvedMonth = (month != null) ? month : now.getMonthValue();
        int resolvedYear = (year != null) ? year : now.getYear();

        try {
            PetStatisticsDTO statistics = healthService.getStatisticsForPet(petId, resolvedMonth, resolvedYear);
            return ResponseEntity.ok(statistics);
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/breeds/{breedId}/health")
    @RolesAllowed("Veterinarian")
    public ResponseEntity<List<BreedHealthInfoDTO>> getHealthInfoForBreed(@PathVariable Long breedId) {
        List<BreedHealthInfo> healthInfos = healthService.getHealthInfoByBreedId(breedId);
        return ResponseEntity.ok(HealthConverter.convertFromBreedHealthInfosToDTOs(healthInfos));
    }

    @PostMapping("/breeds/{breedId}/health")
    @RolesAllowed("Veterinarian")
    public ResponseEntity<Void> createHealthInfoForBreed(@PathVariable Long breedId, @RequestBody CreateBreedHealthInfoDTO breedHealthInfoDTO) {
        try {
            BreedHealthInfo breedHealthInfo = HealthConverter.convertFromCreateBreedHealthInfoDTOToBreedHealthInfo(breedHealthInfoDTO);
            healthService.createHealthInfoForBreed(breedId, breedHealthInfo);
            return ResponseEntity.status(201).build();
        } catch (InvalidBreedException e) {
            return ResponseEntity.badRequest().build();  // Return a 400 Bad Request if the breed is invalid
        }
    }
}
