package com.medbid.medbid.business.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role extends GrantedAuthority {
    private static final String ROLE_PREFIX = "ROLE_";

    @Column(name = "name", unique = true, updatable = false, length = 40, nullable = false)
    private UserRole userRole;

    @Override
    public String getName() {
        return ROLE_PREFIX + userRole.name();
    }
}
