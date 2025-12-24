package com.thaipd.sbjpaprac.common.exception;

import com.thaipd.sbjpaprac.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
	public BadRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, message);
	}
}
