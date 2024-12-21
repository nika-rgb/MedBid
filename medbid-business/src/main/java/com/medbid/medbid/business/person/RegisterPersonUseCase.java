package com.medbid.medbid.business.person;

import com.medbid.medbid.business.user.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterPersonUseCase {
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesRepository grantedAuthoritiesRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerPerson(Person person) {
        if (userRepository.existsByIdNumber(person.getIdNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already created please try to login");
        }

        PersonEntity personEntity = buildPersonEntity(person);
        personRepository.save(personEntity);

        UserEntity userEntity = buildUserEntity(person, personEntity.getId());

        userEntity = userRepository.save(userEntity);

        List <GrantedAuthorities> grantedAuthorities = buildGrantedAuthority(userEntity);
        grantedAuthoritiesRepository.saveAll(grantedAuthorities);
    }

    private List<GrantedAuthorities> buildGrantedAuthority(UserEntity userEntity) {
        GrantedAuthorities grantedAuthority = new GrantedAuthorities();

        grantedAuthority.setUser(userEntity);
        grantedAuthority.setAuthorityType(AuthorityType.ROLE);
        grantedAuthority.setGrantedAuthority(
                roleRepository.getRoleByUserRole(UserRole.USER)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve required roles"))
        );

        return List.of(grantedAuthority);
    }

    private UserEntity buildUserEntity(Person person, UUID mappingId) {
        UserEntity userEntity = new UserEntity();

        userEntity.setIdNumber(person.getIdNumber());
        userEntity.setPassword(passwordEncoder.encode(person.getPassword()));
        userEntity.setUserType(UserType.PERSON);
        userEntity.setVerified(false);
        userEntity.setMappingId(mappingId);

        return userEntity;
    }

    private PersonEntity buildPersonEntity(Person person) {
        PersonEntity personEntity = new PersonEntity();

        personEntity.setIdNumber(person.getIdNumber());
        personEntity.setFirstName(person.getFirstName());
        personEntity.setLastName(person.getLastName());
        personEntity.setBirthDate(person.getBirthDate());
        personEntity.setGender(person.getGender());
        personEntity.setEmail(person.getEmail());
        personEntity.setPhoneNumberPrefix(person.getPhoneNumberPrefix());
        personEntity.setPhoneNumber(person.getPhoneNumber());

        return personEntity;
    }

}
