package com.thaipd.sbjpaprac.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CarEntity {
    private Long id;
    private String color;
    int amountOfSeats;
    int maxSpeed;
}
