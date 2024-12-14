package com.medbid.medbid.api.rest.v1;

import com.medbid.medbid.api.rest.v1.request.PersonRegistrationRequest;
import com.medbid.medbid.api.rest.v1.response.RegistrationSuccessfulResponse;
import com.medbid.medbid.business.person.Gender;
import com.medbid.medbid.business.person.Person;
import com.medbid.medbid.business.person.RegisterPersonUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(name = "/api/v1/medbid/register")
@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegisterPersonUseCase useCase;

    /**
     * Registers the person on the platform, Please see contents for PersonRegistrationRequest.java for exact fields expected to be received
     *
     * @param request Registration request body, containing all required fields for registering a person
     * @return Registration response
     */
    @PostMapping("person")
    public ResponseEntity<RegistrationSuccessfulResponse> registerPerson(@RequestBody PersonRegistrationRequest request) {
        useCase.registerPerson(fromRegisterRequest(request));
        return ResponseEntity.ok(new RegistrationSuccessfulResponse("Person is registered successfully"));
    }

    private Person fromRegisterRequest(PersonRegistrationRequest personRegistrationRequest) {
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
