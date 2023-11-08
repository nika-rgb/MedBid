package com.medbid.MedBid.exception;

import com.medbid.MedBid.business.exception.ApplicationException;
import com.medbid.MedBid.business.exception.ApplicationExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(RestControllerAdvice.class.getSimpleName());

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        ApplicationExceptionEnum exceptionEnum = exception.getExceptionEnum();
        log.info("Application exception caught with code: {}", exceptionEnum.getStatus(), exception);
        ErrorResponse response = buildResponse(exception.getMessage(), exceptionEnum.getStatus());
        return ResponseEntity.status(exceptionEnum.getStatus().value())
            .body(response);
    }

    private ErrorResponse buildResponse(String message, HttpStatus status) {
        return new ErrorResponse(status, message);
    }


}
