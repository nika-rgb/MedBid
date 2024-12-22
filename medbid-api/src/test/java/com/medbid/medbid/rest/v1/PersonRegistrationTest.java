package com.medbid.medbid.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medbid.medbid.business.person.PersonEntity;
import com.medbid.medbid.business.person.PersonRepository;
import com.medbid.medbid.business.user.*;
import com.medbid.medbid.rest.v1.request.PersonRegistrationRequest;
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
class PersonRegistrationTest {
    private static final String REGISTER_PERSON_API_PATH = "/api/v1/medbid/register/person";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerPerson_success() throws Exception {

        PersonRegistrationRequest request = validPersonRegistrationRequest();

        MvcResult result = registerPerson(request);

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        RegistrationResponse response = JsonUtils.convertJsonString(content, RegistrationResponse.class, objectMapper);
        assertEquals(HttpStatus.OK.value(), status);
        assertEquals("Person is registered successfully", response.message());
    }

    @Test
    void registerPerson_success_verify_data() throws Exception {

        PersonRegistrationRequest request = validPersonRegistrationRequest();

        String idNumber = request.idNumber();

        registerPerson(request);

        Optional<PersonEntity> person = personRepository.findByIdNumber(idNumber);
        Optional<UserEntity> user = userRepository.findUserWithGrantedAuthorities(idNumber);

        assertTrue(person.isPresent());
        assertTrue(user.isPresent());

        PersonEntity personEntity = person.get();
        UserEntity userEntity = user.get();

        verifyPerson(personEntity, request);
        verifyUser(userEntity, request, personEntity.getId());
    }

    @Test
    void registerPerson_API_validation_failure() throws Exception {
        PersonRegistrationRequest invalidPersonRegistrationReq = invalidPersonRegistrationRequest();
        MvcResult mvcResult = registerPerson(invalidPersonRegistrationReq);

        ApiErrorResponse response =
                JsonUtils.convertJsonString(mvcResult.getResponse().getContentAsString(), ApiErrorResponse.class, objectMapper);

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        assertEquals(REGISTER_PERSON_API_PATH, response.path());
        assertNotNull(response.requestDate());
    }

    @Test
    void registerPerson_personAlreadyRegistered() throws Exception {
        PersonRegistrationRequest request = validPersonRegistrationRequest();

        MvcResult firstTryResult = registerPerson(request);

        // person registered
        assertEquals(HttpStatus.OK.value(), firstTryResult.getResponse().getStatus());

        MvcResult secondTryResult = registerPerson(request);

        assertEquals(HttpStatus.CONFLICT.value(), secondTryResult.getResponse().getStatus());
    }

    private PersonRegistrationRequest invalidPersonRegistrationRequest() {
        return new PersonRegistrationRequest(
                "",
                "",
                "Alma",
                "Meter",
                LocalDate.now(),
                "MALE",
                "a.m@gmail.com",
                "+995",
                "583635946"
        );
    }

    private PersonRegistrationRequest validPersonRegistrationRequest() {
        return new PersonRegistrationRequest(
                "05693564132",
                "Aa12345@",
                "Lenny",
                "Kratzer",
                LocalDate.now(),
                "MALE",
                "l.k@gmail.com",
                "+995",
                "578236315"
        );
    }

    private void verifyUser(UserEntity userEntity, PersonRegistrationRequest request, UUID mappingId) {
        assertEquals(request.idNumber(), userEntity.getIdNumber());
        assertTrue(passwordEncoder.matches(request.password(), userEntity.getPassword()));
        assertEquals(UserType.PERSON, userEntity.getUserType());
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

                    assertTrue(StringUtils.endsWith(authority.getName(), UserRole.USER.name()));
                }
        );

    }

    private void verifyPerson(PersonEntity personEntity, PersonRegistrationRequest request) {
        assertEquals(request.idNumber(), personEntity.getIdNumber());
        assertEquals(request.email(), personEntity.getEmail());
        assertEquals(request.birthDate(), personEntity.getBirthDate());
        assertEquals(request.firstName(), personEntity.getFirstName());
        assertEquals(request.lastName(), personEntity.getLastName());
        assertEquals(request.gender(), personEntity.getGender().name());
        assertEquals(request.phoneNumber(), personEntity.getPhoneNumber());
        assertEquals(request.phoneNumberPrefix(), personEntity.getPhoneNumberPrefix());
    }


    private MvcResult registerPerson(PersonRegistrationRequest request) throws Exception {
        return mockMvc.perform(
                post(REGISTER_PERSON_API_PATH)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
    }

}
