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

    @Mapping(target = "urlWebsite", source = "website")
    @Mapping(target = "fullName", expression = "java(customer.getFirstName() + \" \" + customer.getLastName())")
    CustomerDTO toDtoBasic(Customer customer);

    default CustomerDTO toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .fullName(customer.getFirstName() + " " + customer.getLastName())
                .address(customer.getAddress())
                .urlWebsite(customer.getWebsite())
                .creditLimit(customer.getCreditLimit())
                .status(customer.getStatus())
                .build();
    }

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "website", source = "urlWebsite")
    Customer toEntity(CustomerDTO customerDTO);

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "website", source = "urlWebsite")
    void updateEntityFromDto(CustomerDTO customerDTO, @MappingTarget Customer customer);
}
