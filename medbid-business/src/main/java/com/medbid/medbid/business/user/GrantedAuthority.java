package com.medbid.medbid.business.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "granted_authority_sequence", sequenceName = "granted_authority_sequence", allocationSize = 1, initialValue = 1)
@Getter
@Setter
public abstract class GrantedAuthority {
    @Id
    @GeneratedValue(generator = "granted_authority_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public abstract String getName();

}
