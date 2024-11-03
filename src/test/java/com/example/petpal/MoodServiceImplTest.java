package com.example.petpal;

import com.example.petpal.business.domain.Mood;
import com.example.petpal.business.impl.MoodServiceImpl;
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

    private Mood mood;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mood = Mood.builder()
                .id(1L)
                .name("Happy")
                .emoji("😊")
                .build();
    }

    @Test
    void getAllMoods_shouldReturnAllMoods() {
        List<Mood> moodList = new ArrayList<>();
        moodList.add(mood);
        when(moodRepository.getAllMoods()).thenReturn(moodList);

        var result = moodService.getAllMoods();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Happy", result.get(0).getName());
        assertEquals("😊", result.get(0).getEmoji());
        verify(moodRepository, times(1)).getAllMoods();
    }

    @Test
    void getMoodById_shouldReturnMoodIfExists() {
        when(moodRepository.getMoodById(1L)).thenReturn(Optional.of(mood));

        var result = moodService.getMoodById(1L);

        assertTrue(result.isPresent());
        assertEquals(mood.getId(), result.get().getId());
        assertEquals("Happy", result.get().getName());
        assertEquals("😊", result.get().getEmoji());
        verify(moodRepository, times(1)).getMoodById(1L);
    }

    @Test
    void getMoodById_shouldReturnEmptyIfNotFound() {
        when(moodRepository.getMoodById(2L)).thenReturn(Optional.empty());

        var result = moodService.getMoodById(2L);

        assertFalse(result.isPresent());
        verify(moodRepository, times(1)).getMoodById(2L);
    }

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
        verify(moodRepository, times(1)).createMood(newMood);
    }

    @Test
    void deleteMood_shouldReturnTrueIfDeleted() {
        when(moodRepository.deleteMood(1L)).thenReturn(true);

        boolean result = moodService.deleteMood(1L);

        assertTrue(result);
        verify(moodRepository, times(1)).deleteMood(1L);
    }

    @Test
    void deleteMood_shouldReturnFalseIfNotDeleted() {
        when(moodRepository.deleteMood(2L)).thenReturn(false);

        boolean result = moodService.deleteMood(2L);

        assertFalse(result);
        verify(moodRepository, times(1)).deleteMood(2L);
    }
}
