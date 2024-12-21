package com.medbid.medbid.rest.v1.handler;

import com.medbid.medbid.rest.v1.response.ApiErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class MedBidExceptionHandler {

    /* TODO
         Add tests too
     */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDetails> handleBadCredentialException(HttpServletRequest request) {
        log.info("Unable to authenticate, with provided credentials please provide the correct ones");
        ApiErrorDetails apiErrorDetails = getApiErrorDetails("Failed to authenticate, incorrect credentials", request.getRequestURI(), null, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorDetails);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorDetails> handleResponseStatusException(ResponseStatusException responseStatusException,  HttpServletRequest request) {
        ApiErrorDetails apiErrorDetails = getApiErrorDetails(responseStatusException.getMessage(), request.getRequestURI(), null, LocalDateTime.now());
        return ResponseEntity.status(responseStatusException.getStatusCode()).body(apiErrorDetails);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDetails> handleHttpMessageNotReadableException(HttpServletRequest request) {
        ApiErrorDetails apiErrorDetails = getApiErrorDetails("Request body is invalid, please make sure to send valid json", request.getRequestURI(), null, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();


        ApiErrorDetails apiErrorDetails = getApiErrorDetails("Provided request body, isn't valid", request.getRequestURI(), errors, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorDetails);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorDetails> handleThrowable(Throwable ex, HttpServletRequest request) {
        log.error("Error without specific handling was thrown", ex);
        ApiErrorDetails apiErrorDetails = getApiErrorDetails("Request failed", request.getRequestURI(), null, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorDetails);
    }

    private ApiErrorDetails getApiErrorDetails(String message, String path, Object errorDetails, LocalDateTime requestDate) {
        return new ApiErrorDetails(message, path, errorDetails, requestDate);
    }

}
