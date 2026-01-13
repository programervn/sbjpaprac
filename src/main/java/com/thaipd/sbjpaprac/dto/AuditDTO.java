package com.thaipd.sbjpaprac.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditDTO implements Serializable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
