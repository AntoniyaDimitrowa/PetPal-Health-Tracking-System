package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

public interface IMoodRepository {
    Optional<MoodEntity> getMoodById(long id);

    ArrayList<MoodEntity> getAllMoods();
    long createMood(MoodEntity mood);

    boolean deleteMood(long id);

}
