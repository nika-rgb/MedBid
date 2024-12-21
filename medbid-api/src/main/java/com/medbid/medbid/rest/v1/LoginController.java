package com.medbid.medbid.rest.v1;

import com.medbid.medbid.rest.v1.auth.AuthenticateUserUseCase;
import com.medbid.medbid.rest.v1.auth.RefreshTokenUseCase;
import com.medbid.medbid.rest.v1.request.AuthenticationRequest;
import com.medbid.medbid.rest.v1.request.RefreshTokenRequest;
import com.medbid.medbid.rest.v1.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1/medbid/auth")
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.refreshToken(request));
    }

}
