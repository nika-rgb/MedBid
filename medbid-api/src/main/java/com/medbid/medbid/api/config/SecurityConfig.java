package com.medbid.medbid.api.config;

import com.medbid.medbid.api.rest.v1.auth.JwtConfigurationProperties;
import com.medbid.medbid.api.rest.v1.auth.JwtTokenUtilities;
import com.medbid.medbid.api.rest.v1.auth.MedbidUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    UserDetailsService medbidUserDetailsService() {
        // PersonRepository will be fetched from context
        return new MedbidUserDetailsService(null);
    }

    @Bean
    @Qualifier("accessTokenUtilities")
    JwtTokenUtilities accessTokenUtilities(JwtConfigurationProperties properties) {
        return new JwtTokenUtilities(properties.getAccessTokenSecretKey(), properties.getAccessTokenExpiresIn());
    }

    @Bean
    @Qualifier("refreshTokenUtilities")
    JwtTokenUtilities refreshTokenUtilities(JwtConfigurationProperties properties) {
        return new JwtTokenUtilities(properties.getRefreshTokenSecretKey(), properties.getRefreshTokenExpiresIn());
    }

}