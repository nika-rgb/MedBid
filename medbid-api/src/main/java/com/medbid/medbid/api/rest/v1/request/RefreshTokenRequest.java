package com.medbid.medbid.api.rest.v1.request;

public record RefreshTokenRequest(
        String accessToken,
        String refreshToken
)
{ }
