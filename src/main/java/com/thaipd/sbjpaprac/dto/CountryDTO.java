package com.thaipd.sbjpaprac.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có đối với JPA Entity
@AllArgsConstructor
public class CountryDTO implements Serializable {
    private Long countryId;
    private String code;
    private String name;
    private String locale;
    private String timeZone;
    private Boolean enabled;

    private CurrencyDTO currency;
    private List<StateDTO> states;
}
