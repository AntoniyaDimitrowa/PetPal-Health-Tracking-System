package com.example.petpal.controller;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.controller.converters.BreedConverter;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.converters.VaccinationConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
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
        PetDTO petDTO = PetConverter.convertFromPetToPetDTO(pet);
        return ResponseEntity.ok(petDTO);
    }

    @PostMapping
    public ResponseEntity<CreateEntityResponse> createPet(@RequestBody CreatePetDTO dto) {
        try {
            Pet newPet = petService.createPet(
                    dto.getName(),
                    dto.getBreedId(),
                    dto.getGender(),
                    dto.getBirthdate(),
                    dto.getWeight(),
                    dto.getVaccinationRecordsIds()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newPet.getId()).build());
        } catch (InvalidBreedException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updatePet(@PathVariable long id, @RequestBody CreatePetDTO dto) {
        try {
            petService.updatePet(id,
                    dto.getName(),
                    dto.getBreedId(),
                    dto.getGender(),
                    dto.getBirthdate(),
                    dto.getWeight());
            return ResponseEntity.noContent().build();
        } catch (InvalidPetException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidBreedException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePet(@PathVariable long id) {
        boolean deleted = petService.deletePet(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
