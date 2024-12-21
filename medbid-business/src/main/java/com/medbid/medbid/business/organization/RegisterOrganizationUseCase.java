package com.medbid.medbid.business.organization;

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
public class RegisterOrganizationUseCase {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesRepository grantedAuthoritiesRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerOrganization(Organization organization) {
        if (organizationRepository.existsByRegistrationNumber(organization.getRegistrationNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Organization is already created please try to login");
        }

        OrganizationEntity organizationEntity = buildOrganizationEntity(organization);

        organizationRepository.save(organizationEntity);

        UserEntity userEntity = buildUserEntity(organization, organizationEntity.getId());

        userRepository.save(userEntity);

        List<GrantedAuthorities> grantedAuthorities = buildGrantedAuthority(userEntity);

        grantedAuthoritiesRepository.saveAll(grantedAuthorities);
    }

    private List<GrantedAuthorities> buildGrantedAuthority(UserEntity userEntity) {
        GrantedAuthorities grantedAuthorities = new GrantedAuthorities();

        grantedAuthorities.setUser(userEntity);
        grantedAuthorities.setAuthorityType(AuthorityType.ROLE);
        grantedAuthorities.setGrantedAuthority(
                roleRepository.getRoleByUserRole(UserRole.ORGANIZATION)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve required roles"))
        );

        return List.of(grantedAuthorities);
    }

    private UserEntity buildUserEntity(Organization organization, UUID mappingId) {
        UserEntity userEntity = new UserEntity();

        userEntity.setIdNumber(organization.getRegistrationNumber());
        userEntity.setPassword(passwordEncoder.encode(organization.getPassword()));
        userEntity.setUserType(UserType.ORGANIZATION);
        userEntity.setMappingId(mappingId);
        userEntity.setVerified(false);

        return userEntity;
    }

    private OrganizationEntity buildOrganizationEntity(Organization organization) {
        OrganizationEntity entity = new OrganizationEntity();

        entity.setName(organization.getOrganizationName());
        entity.setRegistrationNumber(organization.getRegistrationNumber());
        entity.setEmail(organization.getEmail());
        entity.setPhoneNumberPrefix(organization.getPhoneNumberPrefix());
        entity.setPhoneNumber(organization.getPhoneNumber());
        entity.setWebsite(organization.getWebsite());
        entity.setEstablishmentDate(organization.getEstablishmentDate());
        entity.setRegisteredAt(organization.getRegisteredAt());

        return entity;
    }

}
