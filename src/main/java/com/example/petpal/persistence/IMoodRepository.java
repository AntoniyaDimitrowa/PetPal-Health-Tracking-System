package com.example.petpal.persistence;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

public interface IMoodRepository {
    Optional<Mood> getMoodById(long id);

    ArrayList<Mood> getAllMoods();
    Long createMood(Mood mood);

    boolean deleteMood(long id);

}
