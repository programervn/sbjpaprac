package com.thaipd.sbjpaprac.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Base {
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
