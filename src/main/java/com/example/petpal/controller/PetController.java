package com.example.petpal.controller;

import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.exception.*;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import com.example.petpal.controller.dto.pet.UpdatePetDTO;
import jakarta.annotation.security.RolesAllowed;
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
        try {
            Optional<Pet> petOptional = petService.getPet(id);
            if (petOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Pet pet = petOptional.get();
            PetDTO petDTO = PetConverter.convertFromPetToPetDTO(pet);
            return ResponseEntity.ok(petDTO);
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @PostMapping
    @RolesAllowed("Owner")
    public ResponseEntity<CreateEntityResponse> createPet(@RequestBody CreatePetDTO dto) {
        try {
            long newPetId = petService.createPet(
                    PetConverter.convertFromCreatePetDTOToPet(dto),
                    dto.getBreedId(),
                    dto.getVaccinationRecordsIds(),
                    dto.getUserId()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newPetId).build());
        } catch (InvalidUserException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidBreedException | InvalidVaccinationException | CreationFailException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping()
    @RolesAllowed("Owner")
    public ResponseEntity<String> updatePet(@RequestBody UpdatePetDTO dto) {
        try {
            petService.updatePet(
                    PetConverter.convertFromUpdatePetDTOToPet(dto),
                    dto.getBreedId()
            );
            return ResponseEntity.ok("Pet updated successfully");
        } catch (InvalidPetException | InvalidBreedException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("Owner")
    public ResponseEntity<Void> deletePet(@PathVariable long id) {
        try {
            boolean deleted = petService.deletePet(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UnauthorizedDataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }
}
