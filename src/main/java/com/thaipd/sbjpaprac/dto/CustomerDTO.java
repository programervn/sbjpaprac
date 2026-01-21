package com.thaipd.sbjpaprac.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record CustomerDTO(
        Long customerId,
        String name,
        String address,
        String website,
        Long creditLimit
) {}
