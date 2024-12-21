package com.medbid.medbid.rest.v1.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull(message = "Id number property can't be equal to null")
        @NotEmpty(message = "Id number property can't be empty")
        String idNumber,
        @NotNull(message = "Password property can't be null")
        String password
) { }
