package com.example.petpal.controller;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.controller.converters.BreedConverter;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.converters.VaccinationConverter;
import com.example.petpal.controller.dto.CreatePetResponse;
import com.example.petpal.controller.dto.PetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final IPetService petService;

    public PetController(IPetService petService) {
        this.petService = petService;
    }


    @GetMapping("{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable(value = "id") final long id) {
        Optional<Pet> petOptional = petService.getPet(id);
        if (petOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pet pet = petOptional.get();
        PetDTO petDTO = PetConverter.convertFromPetToPetDTO(pet); // Convert to DTO
        return ResponseEntity.ok(petDTO);
    }

    @PostMapping
    public ResponseEntity<CreatePetResponse> createPet(@RequestBody PetDTO dto) {
        Pet newPet = petService.createPet(
                dto.getName(),
                BreedConverter.convertFromBreedDTOToBreed(dto.getBreed()),
                dto.getGender(),
                dto.getBirthdate(),
                dto.getWeight(),
                VaccinationConverter.convertFromVaccinationRecordDTOsToVaccinationRecords(dto.getVaccinationRecords())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CreatePetResponse.builder().id(newPet.getId()).build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updatePet(@PathVariable long id, @RequestBody PetDTO dto) {
        try {
            petService.updatePet(id,
                    dto.getName(),
                    BreedConverter.convertFromBreedDTOToBreed(dto.getBreed()),
                    dto.getGender(),
                    dto.getBirthdate(),
                    dto.getWeight());
            //maybe I should update the VaccinationRecords and Health Records from here as well
            // orr maybe it should be done from the vaccinationService
            return ResponseEntity.noContent().build();
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePet(@PathVariable long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
