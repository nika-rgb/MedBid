package com.medbid.medbid.business.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "application_user")
@Getter
@Setter
@SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", initialValue = 1, allocationSize = 1)
public class UserEntity {

    // TODO Consider using UUID as id since fewer queries will be needed
    // TODO Consider using Projection for user class

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(generator = "user_id_sequence")
    private Long id;

    @Column(name = "id_number", nullable = false, unique = true, length = 50, updatable = false)
    @NaturalId
    private String idNumber;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_type", nullable = false, length = 20, updatable = false)
    private UserType userType;

    @Column(name = "mapping_id", nullable = false, unique = true)
    private UUID mappingId;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<GrantedAuthorities> grantedAuthorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(idNumber, that.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }
}
