package com.example.petpal.controller;

import com.example.petpal.business.IMoodService;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.controller.converters.MoodConverter;
import com.example.petpal.controller.dto.CreateEntityResponse;
import com.example.petpal.controller.dto.mood.MoodDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/moods")
public class MoodController {
    private final IMoodService moodService;

    public MoodController(IMoodService moodService) {
        this.moodService = moodService;
    }

    @GetMapping("{id}")
    public ResponseEntity<MoodDTO> getMood(@PathVariable(value = "id") final long id) {
        Optional<Mood> moodOptional = moodService.getMoodById(id);
        if (moodOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mood mood = moodOptional.get();
        MoodDTO moodDTO = MoodConverter.convertFromMoodToMoodDTO(mood);
        return ResponseEntity.ok(moodDTO);
    }

    @GetMapping()
    public ResponseEntity<ArrayList<MoodDTO>> getAllMoods() {
        ArrayList<MoodDTO> moodDTOs = MoodConverter.convertFromMoodsToMoodDTOs(moodService.getAllMoods());
        return ResponseEntity.ok(moodDTOs);
    }

    @PostMapping
    public ResponseEntity<CreateEntityResponse> createMood(@RequestBody MoodDTO dto) {
        long newMoodId = moodService.createMood(MoodConverter.convertFromMoodDTOToMood(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(CreateEntityResponse.builder().id(newMoodId).build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable long id) {
        boolean deleted = moodService.deleteMood(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
