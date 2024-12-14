package com.medbid.medbid.business.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "person")
@Getter
@Setter
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_number", nullable = false, unique = true, length = 50)
    @NaturalId
    private String idNumber;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 200)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false, length = 5)
    private Gender gender;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    // TODO Consider changing way we store phone number
    @Column(name = "phone_number_prefix", nullable = false)
    private String phoneNumberPrefix; // Can be moved to separate catalog table

    @Column(name = "phone_number", nullable = false, length = 60, unique = true)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // TODO add address field it should be a separate table
    private boolean isVerified;

}


