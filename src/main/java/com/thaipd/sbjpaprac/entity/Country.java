package com.thaipd.sbjpaprac.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "COUNTRY") // This annotation is optionally
@Getter
@Setter
@NoArgsConstructor // Bắt buộc phải có đối với JPA Entity
@AllArgsConstructor
public class Country extends Base implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTRY_ID")
    private Long countryId;

    @Column(name = "CODE", nullable = false, length = 4)
    private String code;
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;
    @Column(name = "LOCALE", nullable = false, length = 6)
    private String locale;
    @Column(name = "TIME_ZONE", nullable = false, length = 10)
    private String timeZone;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = Boolean.TRUE;

    /**
     * Mối quan hệ N-1 với Currency.
     * JoinColumn trỏ tới cột CURRENCY_ID trong bảng COUNTRY.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID", nullable = false)
    private Currency currency;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false, updatable = false, insertable = false)
    @OrderBy(value = "code")
    private List<State> states;
}
