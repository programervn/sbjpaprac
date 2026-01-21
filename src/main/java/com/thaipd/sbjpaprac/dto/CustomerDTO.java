package com.thaipd.sbjpaprac.dto;

public record CustomerDTO(
                Long customerId,
                String name,
                String address,
                String website,
                Long creditLimit) {
}
