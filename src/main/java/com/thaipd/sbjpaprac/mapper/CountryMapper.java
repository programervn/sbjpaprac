package com.thaipd.sbjpaprac.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.thaipd.sbjpaprac.dto.CountryDTO;
import com.thaipd.sbjpaprac.entity.Country;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { StateMapper.class, CurrencyMapper.class })
public interface CountryMapper {

    @Mapping(source = "currency.currencyId", target = "currencyId")
    // thêm dòng này để không lấy dữ liệu currency
    @Mapping(target = "currency", ignore = true)
    CountryDTO toDTO(Country country);

    List<CountryDTO> toDTOList(List<Country> countries);

    @Mapping(target = "version", ignore = true)
    @Mapping(source = "currencyId", target = "currency.currencyId")
    Country toEntity(CountryDTO countryDTO);
}
