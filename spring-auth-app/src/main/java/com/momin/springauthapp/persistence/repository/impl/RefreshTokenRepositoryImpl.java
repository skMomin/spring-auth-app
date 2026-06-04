package com.momin.springauthapp.persistence.repository.impl;

import static com.momin.springauthapp.jooq.tables.RefreshTokens.REFRESH_TOKENS;

import com.momin.springauthapp.auth.RefreshTokenRecord;
import com.momin.springauthapp.persistence.repository.RefreshTokenRepository;
import com.momin.springauthapp.persistence.repository.mappers.RefreshTokenRecordMapper;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final DSLContext dsl;
    private final RefreshTokenRecordMapper refreshTokenRecordMapper;

    public RefreshTokenRepositoryImpl(DSLContext dsl, RefreshTokenRecordMapper refreshTokenRecordMapper) {
        this.dsl = dsl;
        this.refreshTokenRecordMapper = refreshTokenRecordMapper;
    }

    @Override
    public RefreshTokenRecord create(UUID userId, String tokenHash, OffsetDateTime expiresAt) {
        var record = refreshTokenRecordMapper.populateForCreate(
            dsl.newRecord(REFRESH_TOKENS),
            userId,
            tokenHash,
            expiresAt
        );
        record.setId(UUID.randomUUID());
        record.store();
        record.refresh();
        return refreshTokenRecordMapper.toDomain(record);
    }

    @Override
    public Optional<RefreshTokenRecord> findActiveByHash(String tokenHash, OffsetDateTime now) {
        return dsl.selectFrom(REFRESH_TOKENS)
            .where(REFRESH_TOKENS.TOKEN_HASH.eq(tokenHash))
            .and(REFRESH_TOKENS.REVOKED_AT.isNull())
            .and(REFRESH_TOKENS.EXPIRES_AT.gt(now))
            .fetchOptional(refreshTokenRecordMapper::toDomain);
    }

    @Override
    public void revoke(UUID id, OffsetDateTime revokedAt) {
        dsl.update(REFRESH_TOKENS)
            .set(REFRESH_TOKENS.REVOKED_AT, revokedAt)
            .where(REFRESH_TOKENS.ID.eq(id))
            .execute();
    }
}
