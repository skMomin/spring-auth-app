package com.momin.springauthapp.company;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CompanyDto(
    UUID id,
    String name,
    String description,
    UUID ownerId,
    OffsetDateTime createdAt
) {
}
