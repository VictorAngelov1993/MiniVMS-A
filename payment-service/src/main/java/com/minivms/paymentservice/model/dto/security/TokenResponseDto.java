package com.minivms.paymentservice.model.dto.security;

public class TokenResponseDto {
    private String token;
    private String tokenType;

    public TokenResponseDto(String token, String tokenType) {
     this.token = token;
     this.tokenType = tokenType;

    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
