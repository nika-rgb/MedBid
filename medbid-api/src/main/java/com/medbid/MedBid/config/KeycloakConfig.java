package com.medbid.MedBid.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

@KeycloakConfiguration
public class KeycloakConfig {
    @Value("${keycloak.connectTimeout}")
    private long connectTimeout;
    @Value("${keycloak.readTimeout}")
    private long readTimeout;

    @Bean
    public RestTemplate keycloakRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.setReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS));
        builder.setConnectTimeout(Duration.of(connectTimeout, ChronoUnit.SECONDS));
        return builder.build();
    }

    @Bean
    @ConfigurationProperties(prefix = "keycloak")
    public Properties keycloakProperties() {
        return new Properties();
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


    @Bean
    protected SessionRegistry buildSessionRegistry() {
        return new SessionRegistryImpl();
    }

}
