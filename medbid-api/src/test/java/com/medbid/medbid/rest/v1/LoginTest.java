package com.medbid.medbid.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medbid.medbid.rest.v1.auth.JwtTokenUtilities;
import com.medbid.medbid.rest.v1.request.AuthenticationRequest;
import com.medbid.medbid.rest.v1.request.PersonRegistrationRequest;
import com.medbid.medbid.rest.v1.request.RefreshTokenRequest;
import com.medbid.medbid.rest.v1.response.AuthenticationTestResponse;
import com.medbid.medbid.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "classpath:empty_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LoginTest {
    private static final String REGISTER_PERSON_API_PATH = "/api/v1/medbid/register/person";
    private static final String LOGIN_PERSON_API_PATH = "/api/v1/medbid/auth/login";
    private static final String REFRESH_TOKEN_API_PATH = "/api/v1/medbid/auth/refresh";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtilities accessTokenUtilities;
    @Autowired
    private JwtTokenUtilities refreshTokenUtilities;

    @Test
    void authenticationSuccessful() throws Exception {
        PersonRegistrationRequest request = buildRegistrationRequest();

        performPost(REGISTER_PERSON_API_PATH, request);

        AuthenticationRequest authenticationRequest = buildAuthenticationRequest(request);

        MvcResult authenticationResponse = performPost(LOGIN_PERSON_API_PATH, authenticationRequest);

        AuthenticationTestResponse response = JsonUtils.convertJsonString(authenticationResponse.getResponse().getContentAsString(), AuthenticationTestResponse.class, objectMapper);

        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertEquals(240, response.accessTokenExpiresIn());
        assertEquals(10800, response.refreshTokenExpiresIn());
    }

    @Test
    void authenticationFailed_incorrectPassword() throws Exception {
        PersonRegistrationRequest request = buildRegistrationRequest();

        AuthenticationRequest authenticationRequest = buildAuthenticationRequestWithDifferentPassword(request, "different password");

        MvcResult authenticationResponse = performPost(LOGIN_PERSON_API_PATH, authenticationRequest);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), authenticationResponse.getResponse().getStatus());
    }

    @Test
    void refreshToken_successful() throws Exception {

        PersonRegistrationRequest request = buildRegistrationRequest();

        performPost(REGISTER_PERSON_API_PATH, request);

        AuthenticationRequest authenticationRequest = buildAuthenticationRequest(request);

        MvcResult response = performPost(LOGIN_PERSON_API_PATH, authenticationRequest);

        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());

        AuthenticationTestResponse authenticationResponse = JsonUtils.convertJsonString(response.getResponse().getContentAsString(), AuthenticationTestResponse.class, objectMapper);

        RefreshTokenRequest refreshTokenRequest = buildRefreshTokenRequest(authenticationResponse);

        Thread.sleep(1000); // Sleep for 1 second, before regenerating token
        // TODO add awaitility dependency

        MvcResult refreshResponse = performPost(REFRESH_TOKEN_API_PATH, refreshTokenRequest);

        AuthenticationTestResponse refreshTokenResponse =
                JsonUtils.convertJsonString(refreshResponse.getResponse().getContentAsString(), AuthenticationTestResponse.class, objectMapper);

        assertNotNull(refreshTokenResponse.accessToken());
        assertNotNull(refreshTokenResponse.refreshToken());
        assertEquals(authenticationResponse.refreshToken(), refreshTokenResponse.refreshToken());
        assertNotEquals(authenticationResponse.accessToken(), refreshTokenResponse.accessToken());
    }

    private RefreshTokenRequest buildRefreshTokenRequest(AuthenticationTestResponse response) {
        return new RefreshTokenRequest(
                response.accessToken(),
                response.refreshToken()
        );
    }

    private AuthenticationRequest buildAuthenticationRequest(PersonRegistrationRequest request) {
        return new AuthenticationRequest(
                request.idNumber(),
                request.password()
        );
    }

    private AuthenticationRequest buildAuthenticationRequestWithDifferentPassword(PersonRegistrationRequest request, String password) {
        return new AuthenticationRequest(
                request.idNumber(),
                password
        );
    }

    private MvcResult performPost(String apiPath, Object request) throws Exception {
        return mockMvc.perform(
                post(apiPath)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
    }

    private PersonRegistrationRequest buildRegistrationRequest() {
        PersonRegistrationRequest request = new PersonRegistrationRequest(
                "145698526",
                "Aa123456@",
                "Firstname",
                "Lastname",
                LocalDate.now(),
                "MALE",
                "f.l@gmail.com",
                "+995",
                "5631526936"
        );

        return request;
    }

}
