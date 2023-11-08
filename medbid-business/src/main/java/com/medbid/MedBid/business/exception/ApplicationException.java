package com.medbid.MedBid.business.exception;

public class ApplicationException extends RuntimeException {
    private final ApplicationExceptionEnum exceptionEnum;

    public ApplicationException(ApplicationExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    public ApplicationException(String message, ApplicationExceptionEnum exceptionEnum) {
        super(message);
        this.exceptionEnum = exceptionEnum;
    }

    public ApplicationException(String message, Throwable throwable, ApplicationExceptionEnum exceptionEnum) {
        super(message, throwable);
        this.exceptionEnum = exceptionEnum;
    }

    public ApplicationExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }
}
