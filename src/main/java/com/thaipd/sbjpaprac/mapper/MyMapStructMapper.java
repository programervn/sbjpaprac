package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.thaipd.sbjpaprac.dto.CarDTO;
import com.thaipd.sbjpaprac.entity.CarEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MyMapStructMapper {
    CarDTO carEntityToCarDTO(CarEntity carEntity);

    CarEntity toEntity(CarDTO car);
}
