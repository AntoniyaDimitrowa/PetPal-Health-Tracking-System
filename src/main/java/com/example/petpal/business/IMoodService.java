package com.example.petpal.business;

import com.example.petpal.business.domain.Breed;
import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.exception.InvalidBreedException;
import com.example.petpal.business.exception.InvalidMoodException;

import java.util.ArrayList;
import java.util.Optional;

public interface IMoodService {
    ArrayList<Mood> getAllMoods();

    Optional<Mood> getMoodById(long id);

    Mood createMood(Mood mood);

    boolean deleteMood(long id);
}
