package com.thaipd.sbjpaprac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CustomerDTO(
                Long customerId,
                String firstName,
                String lastName,
                String fullName,
                String address,
                String urlWebsite,
                Long creditLimit,
                @JsonProperty("trangthai") Long status) {
}
