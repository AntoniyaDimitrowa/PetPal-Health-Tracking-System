package com.example.petpal.persistence.impl;

import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MoodRepositoryImpl implements IMoodRepository {
    public MoodRepositoryImpl() {}
    @Override
    public Optional<MoodEntity> getMoodById(long id) {
        return Optional.empty();
    }
}
