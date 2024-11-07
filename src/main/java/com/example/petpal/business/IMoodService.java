package com.example.petpal.business;

import com.example.petpal.business.domain.Mood;

import java.util.List;
import java.util.Optional;

public interface IMoodService {
    List<Mood> getAllMoods();

    Optional<Mood> getMoodById(Long id);

    Long createMood(Mood mood);

    boolean deleteMood(Long id);
}
