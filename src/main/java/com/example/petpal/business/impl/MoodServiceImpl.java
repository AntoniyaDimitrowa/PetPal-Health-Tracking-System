package com.example.petpal.business.impl;

import com.example.petpal.business.IMoodService;
import com.example.petpal.business.domain.Mood;
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
        return moodRepository.getAllMoods();
    }

    @Override
    public Optional<Mood> getMoodById(Long id) {
        return moodRepository.getMoodById(id);
    }

    @Override
    public Long createMood(Mood mood) {
        return moodRepository.createMood(mood);
    }

    @Override
    public boolean deleteMood(Long id) {
        return this.moodRepository.deleteMood(id);
    }
}
