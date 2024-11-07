package com.example.petpal.persistence;

import com.example.petpal.business.domain.Mood;

import java.util.List;
import java.util.Optional;

public interface IMoodRepository {
    Optional<Mood> getMoodById(long id);

    List<Mood> getAllMoods();
    Long createMood(Mood mood);

    boolean deleteMood(long id);

}
