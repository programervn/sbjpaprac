package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    @Test
    void getById_ReturnsCountry() throws Exception {
        CountryDTO dto = new CountryDTO();
        dto.setCountryId(1L);
        dto.setCode("VN");
        when(countryService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/country/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryId").value(1))
                .andExpect(jsonPath("$.code").value("VN"));
    }

    @Test
    void getByCode_ReturnsListOfCountries() throws Exception {
        CountryDTO dto = new CountryDTO();
        dto.setCode("VN");
        when(countryService.getByCode("VN")).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/country/code/VN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("VN"));
    }

    @Test
    void save_CreatesCountry() throws Exception {
        CountryDTO dto = new CountryDTO();
        dto.setCode("US");
        when(countryService.save(any(CountryDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/country")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"code\":\"US\", \"name\":\"United States\", \"locale\":\"en_US\", \"timeZone\":\"UTC-5\", \"enabled\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("US"));
    }

    @Test
    void update_UpdatesCountry() throws Exception {
        CountryDTO dto = new CountryDTO();
        dto.setCode("US");
        when(countryService.update(any(CountryDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/country/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"code\":\"US\", \"name\":\"United States\", \"locale\":\"en_US\", \"timeZone\":\"UTC-5\", \"enabled\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("US"));
    }

    @Test
    void delete_ReturnsOk() throws Exception {
        doNothing().when(countryService).delete(anyLong());

        mockMvc.perform(delete("/country/1"))
                .andExpect(status().isOk());
    }
}
