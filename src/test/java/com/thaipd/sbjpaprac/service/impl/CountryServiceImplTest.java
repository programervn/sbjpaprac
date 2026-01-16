package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.mapper.ApiMapper;
import com.thaipd.sbjpaprac.repository.CountryRepository;
import com.thaipd.sbjpaprac.repository.StateRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository repository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private Validator validator;

    @Mock
    private ApiMapper apiMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Test
    void getById_WhenExists_ReturnsDTO() {
        Long id = 1L;
        Country country = new Country();
        country.setCountryId(id);
        country.setCode("VN");
        country.setName("Vietnam");
        CountryDTO dto = new CountryDTO();
        dto.setCountryId(id);
        dto.setCode("VN");

        when(repository.findById(id)).thenReturn(Optional.of(country));
        when(apiMapper.entityToDTO(country)).thenReturn(dto);

        CountryDTO result = countryService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getCountryId());
        assertEquals("VN", result.getCode());
    }

    @Test
    void getById_WhenNotExists_ReturnsNull() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        CountryDTO result = countryService.getById(id);

        assertNull(result);
    }

    @Test
    void getByCode_WhenExists_ReturnsList() {
        String code = "VN";
        Country country = new Country();
        country.setCode(code);
        CountryDTO dto = new CountryDTO();
        dto.setCode(code);
        List<Country> countries = Arrays.asList(country);
        List<CountryDTO> dtos = Arrays.asList(dto);

        when(repository.findByCode(code)).thenReturn(countries);
        when(apiMapper.entityToDTO(countries)).thenReturn(dtos);

        List<CountryDTO> result = countryService.getByCode(code);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(code, result.get(0).getCode());
    }

    @Test
    void getByCode_WhenNotExists_ReturnsEmptyList() {
        String code = "XX";
        when(repository.findByCode(code)).thenReturn(Collections.emptyList());
        when(apiMapper.entityToDTO(Collections.<Country>emptyList())).thenReturn(Collections.emptyList());

        List<CountryDTO> result = countryService.getByCode(code);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_ValidCountry_ReturnsDTO() {
        CountryDTO dto = new CountryDTO();
        dto.setCode("US");

        Country entity = new Country();
        entity.setCode("US");

        when(apiMapper.DTOToEntity(dto)).thenReturn(entity);
        when(repository.save(any(Country.class))).thenReturn(entity);
        when(validator.validate(any(Country.class))).thenReturn(Collections.emptySet());
        when(apiMapper.entityToDTO(entity)).thenReturn(dto);

        CountryDTO result = countryService.save(dto);

        assertNotNull(result);
        assertEquals("US", result.getCode());
        verify(repository).save(any(Country.class));
    }

    @Test
    void delete_WhenExists_SetsEnabledFalse() throws InterruptedException {
        Long id = 1L;
        Country country = new Country();
        country.setCountryId(id);
        country.setEnabled(true);
        when(repository.findById(id)).thenReturn(Optional.of(country));

        countryService.delete(id);

        assertFalse(country.getEnabled());
        verify(repository).save(country);
    }
}
