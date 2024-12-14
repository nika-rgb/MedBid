package com.medbid.medbid.business.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "organization")
@Getter
@Setter
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "registration_number", nullable = false, length = 100)
    private String registrationNumber;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 60)
    private String phoneNumber;

    @Column(name = "website", nullable = false)
    private String website;

}
