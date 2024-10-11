package com.example.petpal.business.impl;

import com.example.petpal.business.IMoodService;
import com.example.petpal.business.converters.BreedConverter;
import com.example.petpal.business.converters.MoodConverter;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.IBreedRepository;
import com.example.petpal.persistence.IMoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MoodServiceImpl implements IMoodService {
    private final IMoodRepository moodRepository;

    @Override
    public ArrayList<Mood> getAllMoods() {
        return MoodConverter.convertFromMoodEntitiesToMoods(moodRepository.getAllMoods());
    }

    @Override
    public Optional<Mood> getMoodById(long id) {
        return moodRepository.getMoodById(id).map(MoodConverter::convertFromMoodEntityToMood);
    }

    @Override
    public Mood createMood(Mood mood) {
        return MoodConverter.convertFromMoodEntityToMood(moodRepository.createMood(MoodConverter.convertFromMoodToMoodEntity(mood)));
    }

    @Override
    public boolean deleteMood(long id) {
        return this.moodRepository.deleteMood(id);
    }
}
