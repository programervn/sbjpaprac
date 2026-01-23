package com.thaipd.sbjpaprac.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String brand, model, color, registrationNumber;
    private Integer modelYear;
    private BigDecimal price;
}
