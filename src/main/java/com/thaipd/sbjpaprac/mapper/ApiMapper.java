package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import com.thaipd.sbjpaprac.entity.Country;
import com.thaipd.sbjpaprac.entity.Currency;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.dto.CurrencyDTO;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    ApiMapper INSTANCE = Mappers.getMapper(ApiMapper.class);

    @Mapping(source = "currencyId", target = "currencyId")
    @Mapping(source = "enabled", target = "enabled")
    CurrencyDTO entityToDTO(Currency currency);

    @Mapping(source = "currencyId", target = "currencyId")
    @Mapping(source = "enabled", target = "enabled")
    Currency DTOToEntity(CurrencyDTO currencyDTO);

    CountryDTO entityToDTO(Country country);

    List<CountryDTO> entityToDTO(List<Country> countries);

    Country DTOToEntity(CountryDTO country);
}
