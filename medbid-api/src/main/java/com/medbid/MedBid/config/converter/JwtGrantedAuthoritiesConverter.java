package com.medbid.MedBid.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String SCOPE_CLAIM = "scope";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String ROLE_PREFIX = "ROLE_";

    private final String clientId;

    public JwtGrantedAuthoritiesConverter(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Collection<String> grantedAuthorities = new ArrayList<>();
        Collection<String> scopes = extractScopes(source);
        Collection<String> roles = extractRoles(source);
        grantedAuthorities.addAll(scopes);
        grantedAuthorities.addAll(roles);
        return grantedAuthorities.stream()
            .map(authority -> (GrantedAuthority) new SimpleGrantedAuthority(authority))
            .toList();
    }

    private Collection<String> extractRoles(Jwt source) {
        Map<String, Map<?, ?>> resourceAccess = (Map<String, Map<?, ?>>) source.getClaims().get(RESOURCE_ACCESS);
        Map <String, List<String>> clientRoles = (Map<String, List<String>>) resourceAccess.get(clientId);


        return clientRoles.get(ROLES_CLAIM)
            .stream()
            .map(role -> ROLE_PREFIX + role)
            .toList();
    }

    private Collection<String> extractScopes(Jwt source) {
        return Arrays.stream(source.getClaims()
                .get(SCOPE_CLAIM)
                .toString()
                .split(" "))
            .map(scope -> SCOPE_PREFIX + scope)
            .toList();
    }
}
