package com.thaipd.sbjpaprac.common.error;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
	private ErrorCode code;
	private String message;
	private String details;
	private int status;
	private String path;
	private Instant timestamp;
}
