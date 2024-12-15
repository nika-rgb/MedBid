package com.medbid.medbid.rest.v1.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;

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
        Claims claims = extractClaims(accessToken);
        return claims.getSubject();
    }


    public boolean isTokenValid(String expectedUsername, String token) {
        Claims claims = extractClaims(token);
        return expectedUsername.equals(claims.getSubject()) && isTokenExpired(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().after(new Date());
    }


    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getHmacKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
