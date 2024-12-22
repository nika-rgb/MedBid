package com.medbid.medbid.rest.v1.response;

public record AuthenticationTestResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn
) {
}
