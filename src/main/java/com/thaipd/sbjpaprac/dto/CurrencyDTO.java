package com.thaipd.sbjpaprac.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO implements Serializable {
    private Long currencyId;
    private String code;
    private String description;
    private Boolean enabled;
    private Integer decimalPlaces;

    private String symbol;

    private AuditDTO audit;
}
