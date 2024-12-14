package com.medbid.medbid.api.rest.v1;

import com.medbid.medbid.api.rest.v1.auth.AuthenticateUserUseCase;
import com.medbid.medbid.api.rest.v1.auth.RefreshTokenUseCase;
import com.medbid.medbid.api.rest.v1.request.AuthenticationRequest;
import com.medbid.medbid.api.rest.v1.request.RefreshTokenRequest;
import com.medbid.medbid.api.rest.v1.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(name = "/api/v1/medbid/auth")
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.authenticationResponse(request));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.refreshToken(request));
    }


    // Implement filter and finalize security config

}
