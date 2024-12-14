package com.medbid.medbid.api.rest.v1.request;

import com.medbid.medbid.api.rest.v1.validation.Password;
import com.medbid.medbid.api.rest.v1.validation.ValueOfEnum;
import com.medbid.medbid.business.person.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record PersonRegistrationRequest(
        @NotNull @NotEmpty String idNumber,
        @NotNull @Password String password,
        @NotNull @Length(min = 2, max = 50, message = "First name length requirements violated [2, 50]") String firstName,
        @NotNull @Length(min = 2, max = 50, message = "Last name length requirements violated [2, 50]") String lastName,
        @NotNull @NotEmpty LocalDate birthDate,
        @NotNull @ValueOfEnum(enumClass = Gender.class) String gender,
        @NotNull @NotEmpty @Email(message = "Invalid format of email") String email,
        @NotNull @Length(min = 2, max = 10, message = "Phone number prefix length requirements violated [2, 10]") String phoneNumberPrefix,
        @NotNull @Length(min = 5, max = 50, message = "Phone number length requirements violated [5, 50]") String phoneNumber
) {
}
