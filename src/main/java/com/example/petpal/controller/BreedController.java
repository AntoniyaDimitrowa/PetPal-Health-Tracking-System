package com.example.petpal.controller;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.controller.converters.*;
import com.example.petpal.controller.dto.*;
import com.example.petpal.controller.dto.breed.BreedDTO;
import com.example.petpal.controller.dto.breed.CreateBreedDTO;
import com.example.petpal.controller.dto.breed.UpdateBreedDTO;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/breeds")
@AllArgsConstructor
public class BreedController {
    private final IBreedService breedService;

    @GetMapping("{id}")
    public ResponseEntity<BreedDTO> getBreed(@PathVariable(value = "id") final long id) {
        Optional<Breed> breedOptional = breedService.getBreedById(id);
        if (breedOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Breed breed = breedOptional.get();
        BreedDTO breedDTO = BreedConverter.convertFromBreedToBreedDTO(breed);
        return ResponseEntity.ok(breedDTO);
    }

    @GetMapping()
    public ResponseEntity<List<BreedDTO>> getAllBreeds() {
        List<BreedDTO> breedDTOs = BreedConverter.convertFromBreedsToBreedDTOs(breedService.getAllBreeds());
        return ResponseEntity.ok(breedDTOs);
    }

    @PostMapping
    @RolesAllowed("Admin")
    public ResponseEntity<CreateEntityResponse> createBreed(@RequestBody CreateBreedDTO dto) {
        try {
            long newBreedId = breedService.createBreed(BreedConverter.convertFromCreateBreedDTOToBreed(dto), dto.getNormalMoodId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newBreedId).build());
        }
        catch (InvalidMoodException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("{id}")
    @RolesAllowed("Admin")
    public ResponseEntity<BreedDTO> updateBreed(@RequestBody UpdateBreedDTO dto, @PathVariable Long id) {
        try {
            breedService.updateBreed(id, BreedConverter.convertFromUpdateBreedDTOToBreed(dto), dto.getNormalMoodId());

            return ResponseEntity.noContent().build();
        } catch (InvalidMoodException | InvalidBreedException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    @RolesAllowed("Admin")
    public ResponseEntity<Void> deleteBreed(@PathVariable long id) {
        boolean deleted = breedService.deleteBreed(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
