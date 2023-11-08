package com.medbid.MedBid.rest.v1.auth;

public class ExchangeCodeRequest {
    private String sessionState;
    private String code;

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ExchangeCodeRequest{" +
            "sessionState='" + sessionState + '\'' +
            ", code='" + code + '\'' +
            '}';
    }
}
