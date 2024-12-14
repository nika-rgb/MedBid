package com.medbid.medbid.api.rest.v1.response;

public record AuthenticationResponse(
        String accessToken,
        long accessTokenExpiresIn,
        String refreshToken,
        long refreshTokenExpiresIn
) {
}
