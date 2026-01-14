package com.thaipd.sbjpaprac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity // This annotation indicates to JPA that it’s something that has a persistent
		// state
@Table(name = "STATE") // This annotation is optionally
@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có đối với JPA Entity
@AllArgsConstructor
public class State extends Base implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STATE_ID")
	private Long stateId;
	@Column(name = "code", nullable = false, length = 6)
	private String code;
	@Column(name = "name", nullable = false, length = 30)
	private String name;
	@Column(name = "enabled", nullable = false)
	private Boolean enabled = Boolean.TRUE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", insertable = false, updatable = false)
	private Country country;
}
