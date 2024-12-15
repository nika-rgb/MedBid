package com.medbid.medbid.rest.v1.request;

public record RefreshTokenRequest(
        String accessToken,
        String refreshToken
)
{ }
