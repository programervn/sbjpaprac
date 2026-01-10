package com.thaipd.sbjpaprac.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CURRENCY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CURRENCY_ID")
    private Long currencyId;

    @Column(name = "CODE", nullable = false, length = 4)
    private String code;

    @Column(name = "SYMBOL", nullable = false, length = 4)
    private String symbol;

    @Column(name = "DESCRIPTION", nullable = false, length = 30)
    private String description;

    @Column(name = "DECIMAL_PLACES", nullable = false)
    private Integer decimalPlaces;

    /**
     * Oracle sử dụng NUMBER(1,0) với CHECK (0,1) để giả lập Boolean.
     * Hibernate tự động mapping 1 -> true, 0 -> false.
     */
    @Builder.Default
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = true;
}