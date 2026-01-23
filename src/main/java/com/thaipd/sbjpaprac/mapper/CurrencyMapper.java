package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.thaipd.sbjpaprac.dto.CurrencyDTO;
import com.thaipd.sbjpaprac.entity.Currency;

// @Mapper(componentModel = "spring")`
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {
    CurrencyDTO toDTO(Currency currency);

    Currency toEntity(CurrencyDTO currency);
}
