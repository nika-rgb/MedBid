package com.medbid.medbid.rest.v1.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtTokenUtilities {
    private final String secretKey;
    private final long expiresIn;

    public JwtTokenUtilities(String secretKey, long expiresIn) {
        this.secretKey = secretKey;
        this.expiresIn = expiresIn;
    }

    public JwtToken generateToken(String username) {
        return generate(username);
    }

    public String getUsernameFromToken(String accessToken) {
        Optional <Claims> claims = extractClaims(accessToken);
        return claims.isEmpty() ? null : claims.get().getSubject();
    }


    public boolean isTokenValid(String token) {
        Optional<Claims> claims = extractClaims(token);
        return claims.isPresent() && isTokenExpired(claims.get());
    }


    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().after(new Date());
    }


    private Optional<Claims> extractClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(getHmacKey(secretKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (ExpiredJwtException exception) {
            log.warn("Provided token is expired");
            return Optional.empty();
        }
    }


    private JwtToken generate(String username) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + expiresIn);
        SecretKey hmacKey = getHmacKey(secretKey);
        String generatedToken = Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(hmacKey)
                .compact();

        return new JwtToken(
                generatedToken,
                expiresIn
        );
    }

    private SecretKey getHmacKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }


public record JwtToken(
        String token,
        long expiresIn
) {
}

}
