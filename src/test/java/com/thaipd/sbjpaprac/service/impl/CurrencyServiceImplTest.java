package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.entity.Currency;
import com.thaipd.sbjpaprac.mapper.ApiMapper;
import com.thaipd.sbjpaprac.repository.CurrencyRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository repository;

    @Mock
    private Validator validator;

    @Mock
    private ApiMapper apiMapper;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    void getById_WhenExists_ReturnsDTO() {
        Long id = 1L;
        Currency currency = new Currency();
        currency.setCurrencyId(id);
        currency.setCode("USD");
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCurrencyId(id);
        currencyDTO.setCode("USD");

        when(repository.findById(id)).thenReturn(Optional.of(currency));
        when(apiMapper.entityToDTO(currency)).thenReturn(currencyDTO);

        CurrencyDTO result = currencyService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getCurrencyId());
        assertEquals("USD", result.getCode());
    }

    @Test
    void getById_WhenNotExists_ReturnsNull() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        CurrencyDTO result = currencyService.getById(id);

        assertNull(result);
    }

    @Test
    void save_ValidCurrency_ReturnsDTO() {
        CurrencyDTO dto = new CurrencyDTO();
        dto.setCode("USD");

        Currency entity = new Currency();
        entity.setCode("USD");

        when(apiMapper.DTOToEntity(dto)).thenReturn(entity);
        when(repository.save(any(Currency.class))).thenReturn(entity);
        when(validator.validate(any(Currency.class))).thenReturn(Collections.emptySet());
        when(apiMapper.entityToDTO(entity)).thenReturn(dto);

        CurrencyDTO result = currencyService.save(dto);

        assertNotNull(result);
        assertEquals("USD", result.getCode());
    }

    @Test
    void delete_WhenExists_SetsEnabledFalse() {
        Long id = 1L;
        Currency currency = new Currency();
        currency.setCurrencyId(id);
        currency.setEnabled(true);
        when(repository.findById(id)).thenReturn(Optional.of(currency));

        currencyService.delete(id);

        assertFalse(currency.getEnabled());
        verify(repository).save(currency);
    }
}
