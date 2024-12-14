package com.medbid.medbid.api.rest.v1.request;

public record AuthenticationRequest(
        String id,
        String password
) { }
