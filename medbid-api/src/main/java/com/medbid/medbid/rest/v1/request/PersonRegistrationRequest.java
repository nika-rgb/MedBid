package com.medbid.medbid.rest.v1.request;

import com.medbid.medbid.rest.v1.validation.Password;
import com.medbid.medbid.rest.v1.validation.ValueOfEnum;
import com.medbid.medbid.business.person.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record PersonRegistrationRequest(
        @NotNull(message = "Id number property can't be null or empty")
        @NotEmpty(message = "Id number property can't be null or empty")
        String idNumber,
        @NotNull(message = "Password property can't be null or empty")
        @Password(message = "Password property can't be null or empty")
        String password,
        @NotNull(message = "First name property can't be null")
        @Length(min = 2, max = 50, message = "First name length requirements violated [2, 50]")
        String firstName,
        @NotNull(message = "Last name property can't be null")
        @Length(min = 2, max = 50, message = "Last name length requirements violated [2, 50]")
        String lastName,
        @NotNull(message = "Birth date property can't be null")
        LocalDate birthDate,
        @NotNull(message = "Gender property can't be null")
        @ValueOfEnum(enumClass = Gender.class, message = "Gender property should be one of (MALE|FEMALE)")
        String gender,
        @NotNull(message = "Email property can't be null or empty")
        @NotEmpty(message = "Email property can't be null or empty")
        @Email(message = "Invalid format of email")
        String email,
        @NotNull(message = "Phone number prefix property can't be null or empty")
        @Length(min = 2, max = 10, message = "Phone number prefix length requirements violated [2, 10]")
        String phoneNumberPrefix,
        @NotNull(message = "Phone number property can't be null or empty")
        @Length(min = 5, max = 50, message = "Phone number length requirements violated [5, 50]")
        String phoneNumber
) {
}
