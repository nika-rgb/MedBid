package com.medbid.medbid.business.person;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegisterPersonUseCase {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerPerson(Person person) {
        if (personRepository.existsByIdNumber(person.getIdNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already created please try to login");
        }

        PersonEntity personEntity = buildPersonEntity(person);

        personRepository.save(personEntity);
    }

    private PersonEntity buildPersonEntity(Person person) {
        PersonEntity personEntity = new PersonEntity();

        personEntity.setIdNumber(person.getIdNumber());
        personEntity.setPassword(passwordEncoder.encode(person.getPassword()));
        personEntity.setFirstName(person.getFirstName());
        personEntity.setLastName(person.getLastName());
        personEntity.setBirthDate(person.getBirthDate());
        personEntity.setGender(person.getGender());
        personEntity.setEmail(person.getEmail());
        personEntity.setPhoneNumberPrefix(person.getPhoneNumberPrefix());
        personEntity.setPhoneNumber(person.getPhoneNumber());
        personEntity.setVerified(person.isVerified());

        return personEntity;
    }

}
