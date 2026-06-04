package com.momin.springauthapp.persistence.repository.mappers;

import com.momin.springauthapp.auth.RefreshTokenRecord;
import com.momin.springauthapp.jooq.tables.records.RefreshTokensRecord;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenRecordMapper {

    public RefreshTokenRecord toDomain(RefreshTokensRecord record) {
        return new RefreshTokenRecord(
            record.getId(),
            record.getUserId(),
            record.getTokenHash(),
            record.getExpiresAt(),
            record.getRevokedAt(),
            record.getCreatedAt()
        );
    }

    public RefreshTokensRecord populateForCreate(
        RefreshTokensRecord record,
        UUID userId,
        String tokenHash,
        OffsetDateTime expiresAt
    ) {
        record.setUserId(userId);
        record.setTokenHash(tokenHash);
        record.setExpiresAt(expiresAt);
        return record;
    }
}
