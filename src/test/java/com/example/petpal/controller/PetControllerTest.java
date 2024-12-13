package com.example.petpal.controller;

import com.example.petpal.business.*;
import com.example.petpal.business.domain.Pet;
import com.example.petpal.business.domain.enums.Gender;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidUserException;
import com.example.petpal.configuration.security.WebSecurityConfig;
import com.example.petpal.configuration.security.token.IAccessTokenDecoder;
import com.example.petpal.configuration.security.token.impl.AccessTokenImpl;
import com.example.petpal.controller.converters.PetConverter;
import com.example.petpal.controller.dto.pet.CreatePetDTO;
import com.example.petpal.controller.dto.pet.PetDTO;
import com.example.petpal.controller.dto.pet.UpdatePetDTO;
import com.example.petpal.controller.dto.breed.BreedDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PetController.class)
@Import({WebSecurityConfig.class}) // Ensure WebSecurityConfig is imported
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPetService petService;

    @MockBean
    private IAccessTokenDecoder accessTokenDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet pet;
    private PetDTO petDTO;
    private CreatePetDTO createPetDTO;
    private UpdatePetDTO updatePetDTO;

    @BeforeEach
    void setUp() {
        pet = Pet.builder()
                .id(1L)
                .name("Buddy")
                .breed(null)
                .gender(Gender.MALE)
                .birthdate(new Date())
                .weight(12.5)
                .image("image_url")
                .vaccinationRecords(Collections.emptyList())
                .healthRecords(Collections.emptyList())
                .build();

        petDTO = PetDTO.builder()
                .id(1L)
                .name("Buddy")
                .breed(BreedDTO.builder().id(1L).name("Golden Retriever").build())
                .gender(Gender.MALE)
                .birthdate(new Date())
                .weight(12.5)
                .image("image_url")
                .vaccinationRecords(Collections.emptyList())
                .healthRecords(Collections.emptyList())
                .build();

        createPetDTO = CreatePetDTO.builder()
                .name("Buddy")
                .breedId(1L)
                .userId(1L)
                .gender(Gender.MALE)
                .birthdate(new Date())
                .weight(12.5)
                .image("image_url")
                .vaccinationRecordsIds(Collections.emptyList())
                .build();

        updatePetDTO = UpdatePetDTO.builder()
                .id(1L)
                .name("Buddy Updated")
                .breedId(1L)
                .gender(Gender.MALE)
                .birthdate(new Date())
                .weight(13.0)
                .image("updated_image_url")
                .build();

        AccessTokenImpl mockToken = new AccessTokenImpl("mock-token", 1L, new ArrayList<>(List.of("Owner")));
        when(accessTokenDecoder.decode(anyString())).thenReturn(mockToken);
    }

    @Test
    void getPet_shouldReturnPetDTO() throws Exception {
        when(petService.getPet(1L)).thenReturn(Optional.of(pet));
        try (MockedStatic<PetConverter> mockedConverter = Mockito.mockStatic(PetConverter.class)) {
            mockedConverter.when(() -> PetConverter.convertFromPetToPetDTO(pet)).thenReturn(petDTO);

            mockMvc.perform(get("/pets/1")
                            .header("Authorization", "Bearer mock-token") // Add Authorization header
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Buddy"));

            verify(petService, times(1)).getPet(1L);
        }
    }

    @Test
    void getPet_notFound() throws Exception {
        when(petService.getPet(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pets/1")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPet(1L);
    }

    @Test
    void createPet_shouldReturnCreatedResponse() throws Exception {
        when(petService.createPet(any(), anyLong(), any(), anyLong())).thenReturn(1L);

        mockMvc.perform(post("/pets")
                        .header("Authorization", "Bearer mock-token") // Token included
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(petService, times(1)).createPet(any(), anyLong(), any(), anyLong());
        verify(accessTokenDecoder, times(1)).decode(anyString());
    }

    @Test
    void createPet_invalidUser_shouldReturnNotFound() throws Exception {
        when(petService.createPet(any(), anyLong(), any(), anyLong())).thenThrow(InvalidUserException.class);

        mockMvc.perform(post("/pets")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPetDTO)))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).createPet(any(), anyLong(), any(), anyLong());
    }

    @Test
    void updatePet_shouldReturnOk() throws Exception {
        // Mock authentication token (ensure correct roles)
        when(accessTokenDecoder.decode(anyString())).thenReturn(new AccessTokenImpl("mock-token", 1L, new ArrayList<>(List.of("Owner"))));

        // Mock the update behavior
        doNothing().when(petService).updatePet(any(), anyLong());

        // Perform the PUT request with Authorization header
        mockMvc.perform(put("/pets")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePetDTO)))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(content().string("Pet updated successfully"));

        // Verify interactions
        verify(petService, times(1)).updatePet(any(), anyLong());
        verify(accessTokenDecoder, times(1)).decode(anyString());
    }

    @Test
    void updatePet_notFound() throws Exception {
        doThrow(InvalidPetException.class).when(petService).updatePet(any(), anyLong());

        mockMvc.perform(put("/pets")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePetDTO)))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).updatePet(any(), anyLong());
    }

    @Test
    void deletePet_shouldReturnNoContent() throws Exception {
        when(petService.deletePet(1L)).thenReturn(true);

        mockMvc.perform(delete("/pets/1")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).deletePet(1L);
    }

    @Test
    void deletePet_notFound() throws Exception {
        when(petService.deletePet(1L)).thenReturn(false);

        mockMvc.perform(delete("/pets/1")
                        .header("Authorization", "Bearer mock-token") // Add Authorization header
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).deletePet(1L);
    }
}
