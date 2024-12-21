package com.medbid.medbid.rest.v1.response;

import java.time.LocalDateTime;

public record ApiErrorDetails(
        String message,
        String path,
        Object additionalDetails,
        LocalDateTime requestDate
) {
}