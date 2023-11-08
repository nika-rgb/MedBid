package com.medbid.MedBid.business.auth;

import com.medbid.MedBid.business.exception.ApplicationException;
import com.medbid.MedBid.business.exception.ApplicationExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthOperations {
    private static final Logger log = LoggerFactory.getLogger(AuthOperations.class.getSimpleName());
    private final AuthService authService;

    public AuthOperations(AuthService authService) {
        this.authService = authService;
    }
    // consider caching the session_state it might be usefull when using refresh token
    public TokenDetails exchangeCode(String sessionState, String code) {
        TokenResponse response = authService.exchangeCode(code);

        if (!response.getSessionState().equals(sessionState)){
            log.info("Session state is changed someone is trying to steal access token");
            throw new ApplicationException("Request is forbidden", ApplicationExceptionEnum.SESSION_STATE_CHANGED);
        }

        return buildTokenDetails(response);
    }

    public void logout(String accessToken, String refreshToken) {
        authService.logout(accessToken, refreshToken);
    }

    public void register(String externalId) {
        log.info("User registered with id {}", externalId);
    }

    public TokenDetails refreshToken(String refreshToken) {
        TokenResponse response = authService.refreshToken(refreshToken);

        return buildTokenDetails(response);
    }

    private TokenDetails buildTokenDetails(TokenResponse response) {
        TokenDetails details = new TokenDetails();

        details.setAccessToken(response.getAccessToken());
        details.setAccessTokenExpiresIn(response.getAccessTokenExpiresIn());
        details.setRefreshToken(response.getRefreshToken());
        details.setRefreshTokenExpiresIn(response.getRefreshTokenExpiresIn());

        return details;
    }


}
