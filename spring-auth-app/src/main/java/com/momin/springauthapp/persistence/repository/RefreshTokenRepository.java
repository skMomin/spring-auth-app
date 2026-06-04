package com.momin.springauthapp.persistence.repository;

import com.momin.springauthapp.auth.RefreshTokenRecord;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    RefreshTokenRecord create(UUID userId, String tokenHash, OffsetDateTime expiresAt);

    Optional<RefreshTokenRecord> findActiveByHash(String tokenHash, OffsetDateTime now);

    void revoke(UUID id, OffsetDateTime revokedAt);
}
