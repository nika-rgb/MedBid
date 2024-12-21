package com.medbid.medbid.business.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "granted_authorities")
@Getter
@Setter
@SequenceGenerator(name = "granted_authorities_id_sequence", sequenceName = "granted_authorities_id_sequence", initialValue = 1, allocationSize = 1)
public class GrantedAuthorities {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(generator = "granted_authorities_id_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserEntity user;

    @Column(name = "authority_type", length = 30, updatable = false, nullable = false)
    private AuthorityType authorityType;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mapping_id", nullable = false, updatable = false, referencedColumnName = "id")
    private GrantedAuthority grantedAuthority;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrantedAuthorities that = (GrantedAuthorities) o;
        return Objects.equals(user.getId(), that.user.getId()) && Objects.equals(grantedAuthority.getId(), that.grantedAuthority.getId()) && authorityType == that.authorityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), grantedAuthority.getId(), authorityType);
    }
}
