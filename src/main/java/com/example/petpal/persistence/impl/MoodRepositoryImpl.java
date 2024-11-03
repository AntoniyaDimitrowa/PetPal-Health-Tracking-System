package com.example.petpal.persistence.impl;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.IMoodRepository;
import com.example.petpal.persistence.IMoodRepositoryJPA;
import com.example.petpal.persistence.converters.MoodConverter;
import com.example.petpal.persistence.entity.MoodEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MoodRepositoryImpl implements IMoodRepository {

    private final IMoodRepositoryJPA moodRepositoryJPA;

    @Autowired
    public MoodRepositoryImpl(IMoodRepositoryJPA moodRepositoryJPA) {
        this.moodRepositoryJPA = moodRepositoryJPA;
    }

    @Override
    public Optional<Mood> getMoodById(long id) {
        Optional<MoodEntity> moodEntityOptional = moodRepositoryJPA.findById(id);
        return moodEntityOptional.map(MoodConverter::convertFromMoodEntityToMood);
    }

    @Override
    public List<Mood> getAllMoods() {
        List<MoodEntity> moodEntities = new ArrayList<>(moodRepositoryJPA.findAll());
        return MoodConverter.convertFromMoodEntitiesToMoods(moodEntities);
    }

    @Override
    public Long createMood(Mood mood) {
        MoodEntity moodEntity = MoodConverter.convertFromMoodToMoodEntity(mood);
        MoodEntity savedMoodEntity = moodRepositoryJPA.save(moodEntity);
        return savedMoodEntity.getId();
    }

    @Override
    public boolean deleteMood(long id) {
        if (moodRepositoryJPA.existsById(id)) {
            moodRepositoryJPA.deleteById(id);
            return true;
        }
        return false;
    }
}
