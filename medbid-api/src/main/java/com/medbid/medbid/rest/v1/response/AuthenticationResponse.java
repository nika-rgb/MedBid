package com.medbid.medbid.rest.v1.response;

public record AuthenticationResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn
) {

    public static final int MILLISECOND_TO_SECONDS = 1000;

    public AuthenticationResponse {
        accessTokenExpiresIn = accessTokenExpiresIn / MILLISECOND_TO_SECONDS; // Convert to seconds
        refreshTokenExpiresIn = refreshTokenExpiresIn / MILLISECOND_TO_SECONDS; // Convert to seconds
    }

}
