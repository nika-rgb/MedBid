package com.medbid.medbid.business.person;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(name = "registeredAt", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    // TODO add address field it should be a separate table

}


