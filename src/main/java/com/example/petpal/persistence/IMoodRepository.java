package com.example.petpal.persistence;

import com.example.petpal.persistence.entity.MoodEntity;

import java.util.Optional;

public interface IMoodRepository {
    Optional<MoodEntity> getMoodById(long id);

}
