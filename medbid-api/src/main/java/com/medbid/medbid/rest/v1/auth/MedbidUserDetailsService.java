package com.medbid.medbid.rest.v1.auth;

import com.medbid.medbid.business.user.GrantedAuthorities;
import com.medbid.medbid.business.user.UserEntity;
import com.medbid.medbid.business.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MedbidUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUserEntity = userRepository.findUserWithGrantedAuthorities(userName);

        if (optionalUserEntity.isPresent()) {
            return constructUserDetails(optionalUserEntity.get());
        }

        throw new UsernameNotFoundException("Authentication failed user not found");
    }

    private UserDetails constructUserDetails(UserEntity userEntity) {
        Set<GrantedAuthorities> grantedAuthorities = userEntity.getGrantedAuthorities();

        Set <GrantedAuthority> authorities = grantedAuthorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getGrantedAuthority().getName()))
                .collect(Collectors.toSet());

        return new User(userEntity.getIdNumber(), userEntity.getPassword(), authorities);
    }

}
