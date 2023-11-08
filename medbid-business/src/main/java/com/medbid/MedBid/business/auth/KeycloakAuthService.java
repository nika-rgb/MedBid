package com.medbid.MedBid.business.auth;

import com.medbid.MedBid.business.exception.ApplicationException;
import com.medbid.MedBid.business.exception.ApplicationExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Properties;

@Service
public class KeycloakAuthService implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(KeycloakAuthService.class.getSimpleName());
    private static final String AUTH_CODE_GRANT_TYPE = "authorization_code";
    private static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";


    private final RestTemplate keycloakRestTemplate;
    private final Properties keycloakProperties;

    public KeycloakAuthService(RestTemplate keycloakRestTemplate, Properties keycloakProperties) {
        this.keycloakRestTemplate = keycloakRestTemplate;
        this.keycloakProperties = keycloakProperties;
    }

    @Override
    public TokenResponse exchangeCode(String code) {
        String tokenUri = keycloakProperties.getProperty("token-uri");
        MultiValueMap<String, String> request = buildTokenRequest(code);

        HttpHeaders headers = buildRequestHeaders(Optional.empty());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(request, headers);

        return sendTokenRequest(tokenUri, httpEntity);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        String logoutUri = keycloakProperties.getProperty("logout-uri");

        MultiValueMap<String, String> request = buildLogoutRequest(refreshToken);

        HttpHeaders headers = buildRequestHeaders(Optional.of(accessToken));

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(request, headers);

        sendLogoutRequest(logoutUri, httpEntity);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        String tokenUri = keycloakProperties.getProperty("token-uri");
        MultiValueMap<String, String> request = buildRefreshTokenRequest(refreshToken);

        HttpHeaders headers = buildRequestHeaders(Optional.empty());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(request, headers);

        return sendRefreshTokenRequest(tokenUri, httpEntity);
    }

    private TokenResponse sendRefreshTokenRequest(String tokenUri, HttpEntity<MultiValueMap<String, String>> httpEntity) {

        try {
            ResponseEntity<TokenResponse> response = keycloakRestTemplate.postForEntity(tokenUri, httpEntity, TokenResponse.class);

            if (response.getStatusCode().isError()) {
                log.error("Failed to refresh token using auth server");
                throw new ApplicationException("Failed to refresh token", ApplicationExceptionEnum.FAILED_TO_REFRESH_TOKEN);
            }

            log.info("Access token is refresshed using auth server");

            return response.getBody();
        } catch (Exception exception) {
            throw new ApplicationException("Unable to refresh access token using auth server", exception, ApplicationExceptionEnum.FAILED_TO_REFRESH_TOKEN);
        }
    }

    private void sendLogoutRequest(String logoutUri, HttpEntity<MultiValueMap<String, String>> httpEntity) {
        try {

            ResponseEntity<Void> response = keycloakRestTemplate.postForEntity(logoutUri, httpEntity, Void.class);

            if (response.getStatusCode().isError()) {
                throw new ApplicationException("Failed to logout ensure that provided parameters are correct", ApplicationExceptionEnum.FAILED_TO_LOGOUT);
            }

            log.info("Successfully logged out from auth server");

        } catch (Exception exception) {
            throw new ApplicationException("Unable to logout", exception, ApplicationExceptionEnum.FAILED_TO_LOGOUT);
        }
    }

    private TokenResponse sendTokenRequest(String tokenUri, HttpEntity<MultiValueMap<String, String>> httpEntity) {
        try {
            ResponseEntity<TokenResponse> response = keycloakRestTemplate.postForEntity(tokenUri, httpEntity, TokenResponse.class);

            if (response.getStatusCode().isError()) {
                log.error("Failed to retrieve access token from auth server");
                throw new ApplicationException("Failed to authorize with given code", ApplicationExceptionEnum.INVALID_CODE);
            }

            log.info("Access token retrieved from auth server");

            return response.getBody();
        } catch (Exception exception) {
            throw new ApplicationException("Unable to retrieve access token from auth server", exception, ApplicationExceptionEnum.INVALID_CODE);
        }
    }

    private HttpHeaders buildRequestHeaders(Optional<String> optionalAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        optionalAccessToken.ifPresent(headers::setBearerAuth);

        return headers;
    }

    private MultiValueMap<String, String> buildTokenRequest(String code) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add("code", code);
        formParameters.add("redirect_uri", keycloakProperties.getProperty("redirect-uri"));
        addClientCredentials(formParameters);
        addGrantType(formParameters, AUTH_CODE_GRANT_TYPE);
        return formParameters;
    }

    private MultiValueMap<String, String> buildLogoutRequest(String refreshToken) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        addClientCredentials(formParameters);
        formParameters.add("refresh_token", refreshToken);
        return formParameters;
    }

    private MultiValueMap<String, String> buildRefreshTokenRequest(String refreshToken) {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        addClientCredentials(formParameters);
        formParameters.add("refresh_token", refreshToken);
        addGrantType(formParameters, REFRESH_TOKEN_GRANT_TYPE);
        return formParameters;
    }



    private void addClientCredentials(MultiValueMap<String, String> formParameters) {
        formParameters.add("client_id", keycloakProperties.getProperty("client.id"));
        formParameters.add("client_secret", keycloakProperties.getProperty("client.secret"));
    }

    private void addGrantType(MultiValueMap<String, String> request, String refreshTokenGrantType) {
        request.add("grant_type", refreshTokenGrantType);
    }


}
