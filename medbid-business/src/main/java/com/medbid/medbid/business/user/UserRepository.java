package com.medbid.medbid.business.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByIdNumber(String idNumber);

    @Query("FROM UserEntity e JOIN fetch e.grantedAuthorities where e.idNumber=:idNumber")
    Optional<UserEntity> findUserWithGrantedAuthorities(String idNumber);

    boolean existsByIdNumber(String idNumber);
}
