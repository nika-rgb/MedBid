package com.medbid.medbid.rest.v1.request;

import com.medbid.medbid.rest.v1.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record OrganizationRegistrationRequest(
        @NotNull(message = "Organization name can't be null or empty")
        @NotEmpty(message = "Organization name can't be null or empty")
        String organizationName,
        @NotNull(message = "Password property can't be null or empty")
        @Password(message = "Password should contain, 1 uppercase 1 lowercase 1 number and 1 symbol character")
        String password,
        @NotNull(message = "Registration number can't be null or empty")
        @Length(min = 2, max = 50, message = "Registration number length requirements violated [2, 50]")
        String registrationNumber,
        @NotNull(message = "Email property can't be null or empty")
        @NotEmpty(message = "Email property can't be null or empty")
        @Email(message = "Invalid format of email")
        String email,
        @NotNull(message = "Phone number prefix property can't be null or empty")
        @Length(min = 2, max = 10, message = "Phone number prefix length requirements violated [2, 10]")
        String phoneNumberPrefix,
        @NotNull(message = "Phone number property can't be null or empty")
        @Length(min = 5, max = 50, message = "Phone number length requirements violated [5, 50]")
        String phoneNumber,
        @Length(min = 2, max = 50, message = "Website length requirements violated [2, 50]")
        String website,
        @NotNull(message = "Birth date property can't be null")
        LocalDate establishmentDate
) {
}
