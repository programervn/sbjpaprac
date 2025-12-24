package com.thaipd.sbjpaprac.common.error;

import java.time.Instant;

public class ApiError {
	private ErrorCode code;
	private String message;
	private String details;
	private int status;
	private String path;
	private Instant timestamp;

	public ApiError() {
	}

	public ApiError(ErrorCode code, String message, String details, int status, String path, Instant timestamp) {
		this.code = code;
		this.message = message;
		this.details = details;
		this.status = status;
		this.path = path;
		this.timestamp = timestamp;
	}

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}
