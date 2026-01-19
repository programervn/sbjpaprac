package com.thaipd.sbjpaprac.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CarDTO {
    private Long id;
    private String color;
    int amountOfSeats;
    int maxSpeed;
}
