package com.thaipd.sbjpaprac.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.thaipd.sbjpaprac.dto.OwnerDTO;
import com.thaipd.sbjpaprac.entity.Owner;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDTO toDTO(Owner owner);

    Owner toEntity(OwnerDTO ownerDTO);

    List<OwnerDTO> toDTOs(List<Owner> owners);

    void updateEntityFromDTO(OwnerDTO ownerDTO, @MappingTarget Owner owner);
}
