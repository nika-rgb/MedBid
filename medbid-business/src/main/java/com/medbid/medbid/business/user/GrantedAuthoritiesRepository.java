package com.medbid.medbid.business.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrantedAuthoritiesRepository extends JpaRepository<GrantedAuthorities, Long> {

}
