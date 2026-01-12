package com.thaipd.sbjpaprac.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có đối với JPA Entity
@AllArgsConstructor
public class City implements Serializable {
	private Long id;
	private String name;
	private Boolean enabled;
	private State state;
}
