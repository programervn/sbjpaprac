package com.thaipd.sbjpaprac.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có đối với JPA Entity
@AllArgsConstructor
public class StateDTO implements Serializable {
	private Long stateId;

	private String code;
	private String name;
	private Boolean enabled;

	private CountryDTO country;
}
