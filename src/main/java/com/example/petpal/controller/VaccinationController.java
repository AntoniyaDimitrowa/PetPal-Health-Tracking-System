package com.example.petpal.controller;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.business.exception.CreationFailException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidVaccinationException;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.converters.VaccinationConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.vaccination.CreateVaccinationRecordDTO;
import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccinations")
@AllArgsConstructor
public class VaccinationController {
    private IVaccinationService vaccinationService;
    @GetMapping()
    public ResponseEntity<List<VaccinationDTO>> getAllVaccinations() {
        List<VaccinationDTO> vaccinationDTOs = VaccinationConverter.convertFromVaccinationsToVaccinationsDTOs(vaccinationService.getVaccinations());
        return ResponseEntity.ok(vaccinationDTOs);
    }

    @PostMapping()
    public ResponseEntity<CreateEntityResponse> createVaccinationRecord (@RequestBody CreateVaccinationRecordDTO dto) {
        try {
            long newVaccinationRecordId = vaccinationService.createVaccinationRecord(
                    dto.getPetId(),
                    dto.getVaccinationId(),
                    dto.getDate()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newVaccinationRecordId).build());
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidVaccinationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
