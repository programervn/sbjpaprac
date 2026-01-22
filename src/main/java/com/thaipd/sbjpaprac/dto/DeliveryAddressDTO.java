package com.thaipd.sbjpaprac.dto;

import lombok.Builder;

@Builder
public record DeliveryAddressDTO(String name, String houseNumber, String city, String state) {
}
