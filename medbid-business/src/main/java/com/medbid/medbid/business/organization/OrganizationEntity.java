package com.medbid.medbid.business.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "registration_number", nullable = false, length = 100, updatable = false)
    private String registrationNumber;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "phoneNumberPrefix", nullable = false, length = 20)
    private String phoneNumberPrefix;

    @Column(name = "phone_number", nullable = false, length = 60)
    private String phoneNumber;

    @Column(name = "website", nullable = false)
    private String website;

    @Column(name = "establishment_date", nullable = false, updatable = false)
    private LocalDate establishmentDate;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

}
