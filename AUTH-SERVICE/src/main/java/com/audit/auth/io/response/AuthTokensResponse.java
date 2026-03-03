package com.audit.auth.io.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokensResponse {

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private long expiresIn;

    private long refreshExpiresIn;
}
