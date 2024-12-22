package com.medbid.medbid.rest.v1.response;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String message,
        String path,
        Object additionalDetails,
        LocalDateTime requestDate
) {
}
