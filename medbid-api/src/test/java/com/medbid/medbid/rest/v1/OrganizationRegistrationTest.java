package com.medbid.medbid.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medbid.medbid.business.organization.OrganizationEntity;
import com.medbid.medbid.business.organization.OrganizationRepository;
import com.medbid.medbid.business.user.*;
import com.medbid.medbid.rest.v1.request.OrganizationRegistrationRequest;
import com.medbid.medbid.rest.v1.response.ApiErrorResponse;
import com.medbid.medbid.rest.v1.response.RegistrationResponse;
import com.medbid.medbid.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:empty_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationRegistrationTest {

    private static final String REGISTER_ORGANIZATION_API_PATH = "/api/v1/medbid/register/organization";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerOrganization_success() throws Exception {

        OrganizationRegistrationRequest request = validOrganizationRegistrationRequest();

        MvcResult result = registerOrganization(request);

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        RegistrationResponse response = JsonUtils.convertJsonString(content, RegistrationResponse.class, objectMapper);
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals("Organization is registered successfully", response.message());
    }

    @Test
    void registerOrganization_success_verify_data() throws Exception {

        OrganizationRegistrationRequest request = validOrganizationRegistrationRequest();

        String registrationNumber = request.registrationNumber();

        MvcResult result = registerOrganization(request);

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        Optional<OrganizationEntity> organization = organizationRepository.findByRegistrationNumber(registrationNumber);
        Optional<UserEntity> user = userRepository.findUserWithGrantedAuthorities(registrationNumber);
        
        assertTrue(organization.isPresent());
        assertTrue(user.isPresent());
        
        OrganizationEntity organizationEntity = organization.get();
        UserEntity userEntity = user.get();
        
        verifyOrganization(organizationEntity, request);
        verifyUser(userEntity, request, organizationEntity.getId());

    }

    @Test
    void registerOrganization_API_validation_failure() throws Exception {
        OrganizationRegistrationRequest invalidPersonRegistrationReq = invalidOrganizationRegistrationRequest();
        MvcResult mvcResult = registerOrganization(invalidPersonRegistrationReq);

        ApiErrorResponse response =
                JsonUtils.convertJsonString(mvcResult.getResponse().getContentAsString(), ApiErrorResponse.class, objectMapper);

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        assertEquals(REGISTER_ORGANIZATION_API_PATH, response.path());
        assertNotNull(response.requestDate());
    }

    @Test
    void registerOrganization_organizationAlreadyRegistered() throws Exception {
        OrganizationRegistrationRequest request = validOrganizationRegistrationRequest();

        MvcResult firstTryResult = registerOrganization(request);

        assertEquals(HttpStatus.OK.value(), firstTryResult.getResponse().getStatus());

        MvcResult secondTryResult = registerOrganization(request);

        assertEquals(HttpStatus.CONFLICT.value(), secondTryResult.getResponse().getStatus());
    }

    private OrganizationRegistrationRequest invalidOrganizationRegistrationRequest() {
        return new OrganizationRegistrationRequest(
                "AA-1234",
                "",
                "",
                "n.c@gmail.com",
                "+885",
                "546235989",
                "https://AA-1234.com",
                LocalDate.now()
        );
    }

    private void verifyUser(UserEntity userEntity, OrganizationRegistrationRequest request, UUID mappingId) {
        assertEquals(request.registrationNumber(), userEntity.getIdNumber());
        assertTrue(passwordEncoder.matches(request.password(), userEntity.getPassword()));
        assertEquals(UserType.ORGANIZATION, userEntity.getUserType());
        assertEquals(mappingId, userEntity.getMappingId());
        assertFalse(userEntity.isVerified());
        assertNotNull(userEntity.getCreatedAt());

        Set<GrantedAuthorities> grantedAuthorities = userEntity.getGrantedAuthorities();

        grantedAuthorities.forEach(
                grantedAuthority -> {
                    GrantedAuthority authority = grantedAuthority.getGrantedAuthority();

                    assertNotNull(grantedAuthority.getId());
                    assertEquals(AuthorityType.ROLE, grantedAuthority.getAuthorityType());
                    assertNotNull(grantedAuthority.getCreatedAt());

                    assertTrue(StringUtils.endsWith(authority.getName(), UserRole.ORGANIZATION.name()));
                }
        );

    }

    private void verifyOrganization(OrganizationEntity organizationEntity, OrganizationRegistrationRequest request) {
        assertEquals(request.organizationName(), organizationEntity.getName());
        assertEquals(request.registrationNumber(), organizationEntity.getRegistrationNumber());
        assertEquals(request.email(), organizationEntity.getEmail());
        assertEquals(request.phoneNumber(), organizationEntity.getPhoneNumber());
        assertEquals(request.phoneNumberPrefix(), organizationEntity.getPhoneNumberPrefix());
        assertEquals(request.website(), organizationEntity.getWebsite());
        assertEquals(request.establishmentDate(), organizationEntity.getEstablishmentDate());
    }

    private MvcResult registerOrganization(OrganizationRegistrationRequest request) throws Exception {
        return mockMvc.perform(
                post(REGISTER_ORGANIZATION_API_PATH)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
    }

    private OrganizationRegistrationRequest validOrganizationRegistrationRequest() {
        OrganizationRegistrationRequest request = new OrganizationRegistrationRequest(
                "Greatest organization",
                "Aa123456@",
                "AA-1234",
                "AA-1234@gmail.com",
                "+995",
                "579989765",
                "https://AA-1234.com",
                LocalDate.now()
        );
        return request;
    }

}
