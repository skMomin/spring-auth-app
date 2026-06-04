package com.momin.springauthapp.auth;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RefreshTokenRecord(
    UUID id,
    UUID userId,
    String tokenHash,
    OffsetDateTime expiresAt,
    OffsetDateTime revokedAt,
    OffsetDateTime createdAt
) {
}
