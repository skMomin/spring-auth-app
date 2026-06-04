package com.momin.springauthapp.auth;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenIssuer {

    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final AuthProperties authProperties;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenIssuer(AuthProperties authProperties, Clock clock) {
        this.authProperties = authProperties;
        this.clock = clock;
    }

    public IssuedRefreshToken issue() {
        var tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);

        return new IssuedRefreshToken(
            BASE64_URL_ENCODER.encodeToString(tokenBytes),
            OffsetDateTime.now(clock).plus(authProperties.refreshTokenTtl())
        );
    }

    public record IssuedRefreshToken(String value, OffsetDateTime expiresAt) {
    }
}
