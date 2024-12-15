package com.medbid.medbid.rest.v1.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "jwt")
@Component
public class JwtConfigurationProperties {
    private String accessTokenSecretKey;
    private long accessTokenExpiresIn;
    private String refreshTokenSecretKey;
    private long refreshTokenExpiresIn;

    public String getAccessTokenSecretKey() {
        return accessTokenSecretKey;
    }

    public void setAccessTokenSecretKey(String accessTokenSecretKey) {
        this.accessTokenSecretKey = accessTokenSecretKey;
    }

    public long getAccessTokenExpiresIn() {
        return accessTokenExpiresIn;
    }

    public void setAccessTokenExpiresIn(long accessTokenExpiresIn) {
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public String getRefreshTokenSecretKey() {
        return refreshTokenSecretKey;
    }

    public void setRefreshTokenSecretKey(String refreshTokenSecretKey) {
        this.refreshTokenSecretKey = refreshTokenSecretKey;
    }

    public long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(long refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
