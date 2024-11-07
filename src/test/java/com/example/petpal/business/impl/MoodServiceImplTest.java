package com.example.petpal.business.impl;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.persistence.IMoodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoodServiceImplTest {

    @Mock
    private IMoodRepository moodRepository;

    @InjectMocks
    private MoodServiceImpl moodService;

    private static final Mood mood = Mood.builder()
            .id(1L)
            .name("Happy")
            .emoji("😊")
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for getAllMoods
    @Test
    void getAllMoods_shouldReturnAllMoods() {
        List<Mood> moodList = new ArrayList<>();
        moodList.add(mood);
        when(moodRepository.getAllMoods()).thenReturn(moodList);

        List<Mood> result = moodService.getAllMoods();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Happy", result.get(0).getName());
        assertEquals("😊", result.get(0).getEmoji());
        verify(moodRepository).getAllMoods();
    }

    @Test
    void getAllMoods_shouldReturnEmptyListIfNoMoods() {
        when(moodRepository.getAllMoods()).thenReturn(new ArrayList<>());

        List<Mood> result = moodService.getAllMoods();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(moodRepository).getAllMoods();
    }

    // Tests for getMoodById
    @Test
    void getMoodById_shouldReturnMoodIfExists() {
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));

        Optional<Mood> result = moodService.getMoodById(1L);

        assertTrue(result.isPresent());
        assertEquals(mood.getId(), result.get().getId());
        assertEquals("Happy", result.get().getName());
        assertEquals("😊", result.get().getEmoji());
        verify(moodRepository).getMoodById(1L);
    }

    @Test
    void getMoodById_shouldReturnEmptyIfNotFound() {
        when(moodRepository.getMoodById(2L)).thenReturn(Optional.empty());

        Optional<Mood> result = moodService.getMoodById(2L);

        assertFalse(result.isPresent());
        verify(moodRepository).getMoodById(2L);
    }

    // Tests for createMood
    @Test
    void createMood_shouldCreateMood() {
        Mood newMood = Mood.builder()
                .name("Excited")
                .emoji("😄")
                .build();
        when(moodRepository.createMood(newMood)).thenReturn(2L);

        Long id = moodService.createMood(newMood);

        assertNotNull(id);
        assertEquals(2L, id);
        verify(moodRepository).createMood(newMood);
    }

    @Test
    void createMood_shouldThrowExceptionIfMoodInvalid() {
        Mood invalidMood = Mood.builder().name("").emoji("").build(); // Invalid mood with empty name/emoji

        when(moodRepository.createMood(invalidMood)).thenThrow(new IllegalArgumentException("Invalid mood data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> moodService.createMood(invalidMood));
        assertEquals("Invalid mood data", exception.getMessage());
        verify(moodRepository).createMood(invalidMood);
    }

    // Tests for deleteMood
    @Test
    void deleteMood_shouldReturnTrueIfDeleted() {
        when(moodRepository.deleteMood(1L)).thenReturn(true);

        boolean result = moodService.deleteMood(1L);

        assertTrue(result);
        verify(moodRepository).deleteMood(1L);
    }

    @Test
    void deleteMood_shouldReturnFalseIfNotDeleted() {
        when(moodRepository.deleteMood(2L)).thenReturn(false);

        boolean result = moodService.deleteMood(2L);

        assertFalse(result);
        verify(moodRepository).deleteMood(2L);
    }
}
