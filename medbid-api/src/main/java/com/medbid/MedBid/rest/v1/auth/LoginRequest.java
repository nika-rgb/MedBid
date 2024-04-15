package com.medbid.MedBid.rest.v1.auth;

public class LoginRequest {
    private String username;
    private byte [] password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte [] getPassword() {
        return password;
    }

    public void setPassword(byte [] password) {
        this.password = password;
    }
}
