package com.example.petpal.controller;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.controller.converters.*;
import com.example.petpal.controller.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/breeds")
public class BreedController {
    private final IBreedService breedService;

    public BreedController(IBreedService breedService) {
        this.breedService = breedService;
    }

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
    public ResponseEntity<ArrayList<BreedDTO>> getAllBreeds() {
        ArrayList<BreedDTO> breedDTOs = BreedConverter.convertFromBreedsToBreedDTOs(breedService.getAllBreeds());
        return ResponseEntity.ok(breedDTOs);
    }

    @PostMapping
    public ResponseEntity<CreateEntityResponse> createBreed(@RequestBody BreedDTO dto) {
        try {
            Breed newBreed = breedService.createBreed(BreedConverter.convertFromBreedDTOToBreed(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newBreed.getId()).build());
        }
        catch (InvalidMoodException e) {
            return ResponseEntity.notFound().build();
        }

    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable long id) {
        boolean deleted = breedService.deleteBreed(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
