package com.audit.auth.security;

import com.audit.auth.exceptions.BadRequestException;
import com.audit.auth.exceptions.UnauthorizedException;
import com.audit.auth.io.response.AuthTokensResponse;
import com.audit.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;

    private final long accessExpirationMs;

    private final long refreshExpirationMs;

    public JwtService(
            @Value("${auth.jwt.secret:}") String secret,
            @Value("${auth.jwt.expiration:3600000}") long accessExpirationMs,
            @Value("${auth.jwt.refresh-expiration:2592000000}") long refreshExpirationMs
    ) {
        if (secret == null || secret.isBlank()) {
            this.key = null;
        } else {
            byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
            if (secretBytes.length < 32) {
                throw new BadRequestException("auth.jwt.secret must be at least 32 characters for HS256");
            }
            this.key = Keys.hmacShaKeyFor(secretBytes);
        }
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public AuthTokensResponse issueTokens(User user) {
        if (key == null) {
            throw new BadRequestException("JWT secret is not configured (auth.jwt.secret)");
        }

        String access = generateToken(user.getId(), "access", accessExpirationMs);
        String refresh = generateToken(user.getId(), "refresh", refreshExpirationMs);

        return AuthTokensResponse.builder()
                .tokenType("Bearer")
                .accessToken(access)
                .refreshToken(refresh)
                .expiresIn(accessExpirationMs / 1000)
                .refreshExpiresIn(refreshExpirationMs / 1000)
                .build();
    }

    public UUID validateRefreshTokenAndGetUserId(String token) {
        Claims claims = parseAndValidate(token, "refresh");
        String sub = claims.getSubject();
        try {
            return UUID.fromString(sub);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token subject");
        }
    }

    private String generateToken(UUID userId, String type, long expirationMs) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(expirationMs);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    private Claims parseAndValidate(String token, String expectedType) {
        if (key == null) {
            throw new BadRequestException("JWT secret is not configured (auth.jwt.secret)");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            if (type == null || !type.equals(expectedType)) {
                throw new UnauthorizedException("Invalid token type");
            }

            return claims;
        } catch (JwtException ex) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }
}
