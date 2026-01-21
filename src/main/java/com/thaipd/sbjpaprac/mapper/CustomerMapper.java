package com.thaipd.sbjpaprac.mapper;

import com.thaipd.sbjpaprac.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import com.thaipd.sbjpaprac.entity.Customer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    // Nếu muốn dùng instance thay vì inject
    // CustomerMapper INSTANCE = Mappers.getMapper( CustomerMapper.class);

    CustomerDTO toDto(Customer customer);

    @Mapping(target = "customerId", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(target = "customerId", ignore = true)
    void updateEntityFromDto(CustomerDTO customerDTO, @MappingTarget Customer customer);
}
