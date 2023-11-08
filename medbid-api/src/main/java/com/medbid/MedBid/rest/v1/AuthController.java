package com.medbid.MedBid.rest.v1;

import com.medbid.MedBid.business.auth.AuthOperations;
import com.medbid.MedBid.business.auth.TokenDetails;
import com.medbid.MedBid.rest.v1.auth.ExchangeCodeRequest;
import com.medbid.MedBid.rest.v1.auth.LogoutRequest;
import com.medbid.MedBid.rest.v1.auth.RefreshTokenRequest;
import com.medbid.MedBid.rest.v1.auth.RegisterUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthOperations authOperations;

    public AuthController(AuthOperations authOperations) {
        this.authOperations = authOperations;
    }

    @PostMapping("/exchange-code")
    public ResponseEntity<TokenDetails> exchangeCode(@RequestBody ExchangeCodeRequest request) {
        return ResponseEntity.ok(authOperations.exchangeCode(request.getSessionState(), request.getCode()));
    }

    // TODO Flow:
    // 1) After user registration id is received here
    // 2) We have profile endpoint where we aggregate data from db and keycloak
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request) {
        authOperations.register(request.getUserId());
        return ResponseEntity.ok(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request, @AuthenticationPrincipal Jwt jwt) {
        authOperations.logout(jwt.getTokenValue(), request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDetails> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenDetails tokenDetails = authOperations.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokenDetails);
    }

    // TODO What I could implement is that if use tried to login keycloak will send a request and save in cache then when trying to exchange code I will first check if he really logged in or not.
    // add validations
}


/*
    T0
 */