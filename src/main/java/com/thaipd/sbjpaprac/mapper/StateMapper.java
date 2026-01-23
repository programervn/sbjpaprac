package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.thaipd.sbjpaprac.dto.StateDTO;
import com.thaipd.sbjpaprac.entity.State;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StateMapper {

    @Mapping(target = "country", ignore = true) // Exclude the country element to prevent recursive mapping
    StateDTO toDTO(State state);

    State toEntity(StateDTO stateDTO);
}
