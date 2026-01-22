package com.thaipd.sbjpaprac.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.thaipd.sbjpaprac.dto.DeliveryAddressDTO;
import com.thaipd.sbjpaprac.entity.Address;
import com.thaipd.sbjpaprac.entity.Customer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryAddressMapper {
    // @Mapping(source = "customer.firstName", target = "name")
    // Below is error because of visibility of fields
    // @Mapping(expression = "java(customer.firstName + \" \" + customer.lastName)",
    // target = "name")
    @Mapping(expression = "java(customer.getFirstName() + \" \" + customer.getLastName())", target = "name")
    @Mapping(source = "shippingAddress.houseNo", target = "houseNumber")
    // @Mapping(source = "shippingAddress.city", target = "city")
    // @Mapping(source = "shippingAddress.state", target = "state")
    DeliveryAddressDTO getDeliveryAddress(Customer customer, Address shippingAddress);
}
