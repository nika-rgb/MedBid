package com.medbid.medbid.rest.v1.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "Access token property can't be null or empty")
        @NotEmpty(message = "Access token property can't be null or empty")
        String accessToken,
        @NotNull(message = "Refresh token property can't be null or empty")
        @NotEmpty(message = "Refresh token property can't be null or empty")
        String refreshToken
)
{ }
