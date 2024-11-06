package com.example.petpal.controller;

import com.example.petpal.business.IVaccinationService;
import com.example.petpal.controller.converters.VaccinationConverter;
import com.example.petpal.controller.dto.vaccination.VaccinationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
