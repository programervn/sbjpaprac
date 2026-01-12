package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.entity.Currency;
import com.thaipd.sbjpaprac.entity.State;
import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.dto.StateDTO;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

    CurrencyDTO entityToDTO(Currency currency);

    Currency DTOToEntity(CurrencyDTO currency);

    CountryDTO entityToDTO(Country country);

    List<CountryDTO> entityToDTO(List<Country> countries);

    @Mapping(target = "states", ignore = true)
    Country DTOToEntity(CountryDTO country);

    @Mapping(target = "country", ignore = true) // Exclude the country element to prevent a recursive mapping
    StateDTO stateToStateDTO(State state);

    State stateDTOToState(StateDTO state);
}
