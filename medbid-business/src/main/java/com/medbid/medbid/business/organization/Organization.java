package com.medbid.medbid.business.organization;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Organization {

    private UUID id;
    private String organizationName;
    private String password;
    private String registrationNumber;
    private String email;
    private String phoneNumberPrefix;
    private String phoneNumber;
    private String website;
    private LocalDate establishmentDate;
    private LocalDateTime registeredAt;
    private boolean isVerified;

}
