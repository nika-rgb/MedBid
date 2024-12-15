package com.medbid.medbid.rest.v1.request;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull String idNumber,
        @NotNull String password
) { }
