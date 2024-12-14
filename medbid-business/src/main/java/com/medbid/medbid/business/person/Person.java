package com.medbid.medbid.business.person;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Person {
    private UUID id;
    private String idNumber;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String phoneNumberPrefix;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private boolean isVerified;
}
