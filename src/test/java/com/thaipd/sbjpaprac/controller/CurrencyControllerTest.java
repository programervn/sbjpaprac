package com.thaipd.sbjpaprac.controller;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
    }

    @Test
    void getById_ReturnsCurrency() throws Exception {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCurrencyId(1L);
        dto.setCode("USD");
        when(currencyService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/currency/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyId").value(1))
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void save_CreatesCurrency() throws Exception {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("USD");
        when(currencyService.save(any(CurrencyDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/currency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"code\":\"USD\", \"description\":\"US Dollar\", \"symbol\":\"$\", \"decimalPlaces\":2, \"enabled\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void update_UpdatesCurrency() throws Exception {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("USD");
        when(currencyService.update(any(CurrencyDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/currency/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"code\":\"USD\", \"description\":\"US Dollar\", \"symbol\":\"$\", \"decimalPlaces\":2, \"enabled\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void delete_ReturnsOk() throws Exception {
        doNothing().when(currencyService).delete(anyLong());

        mockMvc.perform(delete("/currency/1"))
                .andExpect(status().isOk());
    }
}
