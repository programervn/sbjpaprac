package com.thaipd.sbjpaprac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thaipd.sbjpaprac.dto.ValidationErrorDTO;
import com.thaipd.sbjpaprac.dto.ViolationDTO;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorDTO onConstraintValidationException(
            ConstraintViolationException e) {
        log.error("Validation error: {}", e.getMessage());
        ValidationErrorDTO error = new ValidationErrorDTO();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new ViolationDTO(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }
}
