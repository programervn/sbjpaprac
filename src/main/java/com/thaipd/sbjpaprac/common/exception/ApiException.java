package com.thaipd.sbjpaprac.common.exception;

import com.thaipd.sbjpaprac.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

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

	public HttpStatus getStatus() {
		return status;
	}

	public ErrorCode getCode() {
		return code;
	}

	public String getDetails() {
		return details;
	}
}
