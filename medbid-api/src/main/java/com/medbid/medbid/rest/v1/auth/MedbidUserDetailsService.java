package com.medbid.medbid.rest.v1.auth;

import com.medbid.medbid.business.person.PersonEntity;
import com.medbid.medbid.business.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class MedbidUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<PersonEntity> optionalPersonEntity = personRepository.findByIdNumber(userName);

        if (optionalPersonEntity.isPresent()) {
            return constructFromPerson(optionalPersonEntity.get());
        }

        throw new UsernameNotFoundException("Authentication failed user not found");
    }

    private UserDetails constructFromPerson(PersonEntity personEntity) {
        return new User(personEntity.getIdNumber(), personEntity.getPassword(), Collections.emptyList());
    }

}
