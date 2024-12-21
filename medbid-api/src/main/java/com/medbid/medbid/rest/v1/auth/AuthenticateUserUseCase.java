package com.medbid.medbid.rest.v1.auth;

import com.medbid.medbid.rest.v1.request.AuthenticationRequest;
import com.medbid.medbid.rest.v1.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtilities accessTokenUtilities;
    private final JwtTokenUtilities refreshTokenUtilities;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authenticationResult = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.idNumber(), request.password())
        );

        UserDetails userDetails = (UserDetails) authenticationResult.getPrincipal();

        JwtTokenUtilities.JwtToken accessToken = accessTokenUtilities.generateToken(userDetails.getUsername());
        JwtTokenUtilities.JwtToken refreshToken = refreshTokenUtilities.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(accessToken.token(), accessToken.expiresIn(), refreshToken.token(), refreshToken.expiresIn());
    }

}
