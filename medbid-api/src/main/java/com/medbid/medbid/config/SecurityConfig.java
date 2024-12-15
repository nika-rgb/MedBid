package com.medbid.medbid.config;

import com.medbid.medbid.business.person.PersonRepository;
import com.medbid.medbid.rest.v1.auth.AuthenticationFilter;
import com.medbid.medbid.rest.v1.auth.JwtConfigurationProperties;
import com.medbid.medbid.rest.v1.auth.JwtTokenUtilities;
import com.medbid.medbid.rest.v1.auth.MedbidUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PersonRepository personRepository;
    private final JwtConfigurationProperties properties;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests.requestMatchers(
                        "/api/v1/medbid/auth/**",
                                "/api/v1/medbid/register/**",
                                "/actuator/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter(null, null), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


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
        return new MedbidUserDetailsService(personRepository);
    }

    @Bean
    @Qualifier("accessTokenUtilities")
    JwtTokenUtilities accessTokenUtilities() {
        return new JwtTokenUtilities(properties.getAccessTokenSecretKey(), properties.getAccessTokenExpiresIn());
    }

    @Bean
    @Qualifier("refreshTokenUtilities")
    JwtTokenUtilities refreshTokenUtilities() {
        return new JwtTokenUtilities(properties.getRefreshTokenSecretKey(), properties.getRefreshTokenExpiresIn());
    }

    @Bean
    AuthenticationFilter  authenticationFilter(JwtTokenUtilities accessTokenUtilities, UserDetailsService medbidUserDetailsService) {
        return new AuthenticationFilter(accessTokenUtilities(), medbidUserDetailsService());
    }

}