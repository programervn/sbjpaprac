package com.thaipd.sbjpaprac.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
        logger.warn("ResourceNotFoundException(String message)");
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn("ResourceNotFoundException(String message, Throwable cause)");
    }
}
