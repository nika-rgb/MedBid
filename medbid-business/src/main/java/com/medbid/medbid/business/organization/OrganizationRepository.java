package com.medbid.medbid.business.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {

    boolean existsByRegistrationNumber(String registrationNumber);

    Optional <OrganizationEntity> findByRegistrationNumber(String registrationNumber);


}
