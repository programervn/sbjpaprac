package com.thaipd.sbjpaprac.common.error;

import com.thaipd.sbjpaprac.common.api.ApiResponse;
import com.thaipd.sbjpaprac.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.transaction.TransactionTimedOutException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex, HttpServletRequest request) {
		ApiError error = new ApiError(
				ex.getCode(),
				ex.getMessage(),
				ex.getDetails(),
				ex.getStatus().value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(ex.getStatus()).body(ApiResponse.fail(error));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		String details = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatFieldError)
				.collect(Collectors.joining(", "));

		ApiError error = new ApiError(
				ErrorCode.VALIDATION_ERROR,
				"Validation failed",
				details,
				HttpStatus.BAD_REQUEST.value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {
		ApiError error = new ApiError(
				ErrorCode.VALIDATION_ERROR,
				"Validation failed",
				ex.getConstraintViolations().stream()
						.map(v -> v.getPropertyPath() + ": " + v.getMessage())
						.collect(Collectors.joining(", ")),
				HttpStatus.BAD_REQUEST.value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(error));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception ex, HttpServletRequest request) {
		ApiError error = new ApiError(
				ErrorCode.INTERNAL_ERROR,
				"Internal server error",
				ex.getClass().getSimpleName(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(error));
	}

	@ExceptionHandler({
			QueryTimeoutException.class,
			TransactionTimedOutException.class,
			jakarta.persistence.QueryTimeoutException.class
	})
	public ResponseEntity<ApiResponse<Void>> handleTimeoutException(Exception ex, HttpServletRequest request) {
		ApiError error = new ApiError(
				ErrorCode.TIMEOUT_ERROR,
				"The operation timed out. Please try again later.",
				ex.getMessage(),
				HttpStatus.REQUEST_TIMEOUT.value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(ApiResponse.fail(error));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException ex,
			HttpServletRequest request) {
		if (ex.getCause() instanceof java.sql.SQLTimeoutException
				|| (ex.getMessage() != null && ex.getMessage().contains("Socket read interrupted"))) {
			return handleTimeoutException(ex, request);
		}

		ApiError error = new ApiError(
				ErrorCode.INTERNAL_ERROR,
				"A database error occurred.",
				ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				request.getRequestURI(),
				Instant.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(error));
	}

	private String formatFieldError(FieldError e) {
		return e.getField() + ": " + e.getDefaultMessage();
	}
}
