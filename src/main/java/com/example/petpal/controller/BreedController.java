package com.example.petpal.controller;

import com.example.petpal.business.IBreedService;
import com.example.petpal.business.IPetService;
import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.User;
import com.example.petpal.business.exception.InvalidMoodException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.controller.converters.BreedConverter;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.converters.UserConverter;
import com.example.petpal.controller.converters.VaccinationConverter;
import com.example.petpal.controller.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
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

}
