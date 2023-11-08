package com.medbid.MedBid.business.auth;


public interface AuthService {
    TokenResponse exchangeCode(String code);

    void logout(String accessToken, String refreshToken);

    TokenResponse refreshToken(String refreshToken);
}
