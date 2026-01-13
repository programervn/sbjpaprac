package com.thaipd.sbjpaprac.common.exception;

import com.thaipd.sbjpaprac.common.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
	private final HttpStatus status;
	private final ErrorCode code;
	private final String details;

	public ApiException(HttpStatus status, ErrorCode code, String message) {
		this(status, code, message, null);
	}

	public ApiException(HttpStatus status, ErrorCode code, String message, String details) {
		super(message);
		this.status = status;
		this.code = code;
		this.details = details;
	}

}
