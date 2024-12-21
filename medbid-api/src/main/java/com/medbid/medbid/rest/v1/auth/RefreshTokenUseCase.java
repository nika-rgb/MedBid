package com.medbid.medbid.rest.v1.auth;

import com.medbid.medbid.rest.v1.request.RefreshTokenRequest;
import com.medbid.medbid.rest.v1.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final JwtTokenUtilities refreshTokenUtilities;
    private final JwtTokenUtilities accessTokenUtilities;
    private final JwtConfigurationProperties jwtConfigurationProperties;

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        if (!refreshTokenUtilities.isTokenValid(request.refreshToken())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Failed to extend token lifecycle");
        }

        String username = refreshTokenUtilities.getUsernameFromToken(request.refreshToken());

        JwtTokenUtilities.JwtToken newAccessToken = accessTokenUtilities.generateToken(username);
        return new AuthenticationResponse(newAccessToken.token(), newAccessToken.expiresIn(), request.refreshToken(), jwtConfigurationProperties.getRefreshTokenExpiresIn());
    }

}
