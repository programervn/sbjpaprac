package com.thaipd.sbjpaprac.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarSearchCriteria {
    private String brand;
    private String model;
    private String color;
    private Integer minYear;
    private Integer maxYear;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
