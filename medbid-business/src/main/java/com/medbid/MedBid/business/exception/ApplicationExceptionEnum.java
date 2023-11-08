package com.medbid.MedBid.business.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationExceptionEnum {
    INVALID_CODE(HttpStatus.BAD_REQUEST, "Invalid authorization code"),
    FAILED_TO_RETRIEVE_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve token"),
    SESSION_STATE_CHANGED(HttpStatus.FORBIDDEN, "Session state is changed ensure that web security works as expected"),
    FAILED_TO_LOGOUT(HttpStatus.NOT_ACCEPTABLE, "Failed to logout ensure that parameters are correct"),
    FAILED_TO_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Invalid refresh token");

    private final HttpStatus status;
    private final String message;

    ApplicationExceptionEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
