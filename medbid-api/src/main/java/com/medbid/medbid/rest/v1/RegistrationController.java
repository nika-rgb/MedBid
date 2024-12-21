package com.medbid.medbid.rest.v1;

import com.medbid.medbid.business.organization.Organization;
import com.medbid.medbid.business.organization.RegisterOrganizationUseCase;
import com.medbid.medbid.rest.v1.request.OrganizationRegistrationRequest;
import com.medbid.medbid.rest.v1.request.PersonRegistrationRequest;
import com.medbid.medbid.rest.v1.response.RegistrationSuccessfulResponse;
import com.medbid.medbid.business.person.Gender;
import com.medbid.medbid.business.person.Person;
import com.medbid.medbid.business.person.RegisterPersonUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1/medbid/register")
@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegisterPersonUseCase registerPersonUseCase;
    private final RegisterOrganizationUseCase registerOrganizationUseCase;

    /**
     * Registers the person on the platform, Please see contents for PersonRegistrationRequest.java for exact fields expected to be received
     *
     * @param request Registration request body, containing all required fields for registering a person
     * @return Registration response
     */
    @PostMapping("/person")
    public ResponseEntity<RegistrationSuccessfulResponse> registerPerson(@RequestBody @Valid PersonRegistrationRequest request) {
        registerPersonUseCase.registerPerson(fromPersonRegisterRequest(request));
        return ResponseEntity.ok(new RegistrationSuccessfulResponse("Person is registered successfully"));
    }

    @PostMapping("/organization")
    public ResponseEntity<RegistrationSuccessfulResponse> registerOrganization(@RequestBody @Valid OrganizationRegistrationRequest request) {
        registerOrganizationUseCase.registerOrganization(fromOrganizationRegisterRequest(request));
        return ResponseEntity.ok(new RegistrationSuccessfulResponse("Organization is registered successfully"));
    }

    private Organization fromOrganizationRegisterRequest(OrganizationRegistrationRequest request) {
        Organization organization = new Organization();

        organization.setOrganizationName(request.organizationName());
        organization.setPassword(request.password());
        organization.setRegistrationNumber(request.registrationNumber());
        organization.setEmail(request.email());
        organization.setPhoneNumberPrefix(request.phoneNumberPrefix());
        organization.setPhoneNumber(request.phoneNumber());
        organization.setWebsite(request.website());
        organization.setEstablishmentDate(request.establishmentDate());

        return organization;
    }

    private Person fromPersonRegisterRequest(PersonRegistrationRequest personRegistrationRequest) {
        Person person = new Person();

        person.setIdNumber(personRegistrationRequest.idNumber());
        person.setPassword(personRegistrationRequest.password());
        person.setFirstName(personRegistrationRequest.firstName());
        person.setLastName(personRegistrationRequest.lastName());
        person.setBirthDate(personRegistrationRequest.birthDate());
        person.setGender(Gender.valueOf(personRegistrationRequest.gender()));
        person.setEmail(personRegistrationRequest.email());
        person.setPhoneNumberPrefix(personRegistrationRequest.phoneNumberPrefix());
        person.setPhoneNumber(personRegistrationRequest.phoneNumber());
        person.setVerified(false);

        return person;
    }

}
