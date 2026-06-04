package com.momin.springauthapp.persistence.repository.mappers;

import com.momin.springauthapp.company.CompanyDto;
import com.momin.springauthapp.jooq.tables.records.CompaniesRecord;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CompanyRecordMapper {

    public CompanyDto toDto(CompaniesRecord record) {
        return new CompanyDto(
            record.getId(),
            record.getName(),
            record.getDescription(),
            record.getOwnerId(),
            record.getCreatedAt()
        );
    }

    public CompaniesRecord populateForCreate(
        CompaniesRecord record,
        UUID ownerId,
        String name,
        String description
    ) {
        record.setOwnerId(ownerId);
        record.setName(name);
        record.setDescription(description);
        return record;
    }

    public CompaniesRecord mergeForUpdate(CompaniesRecord record, String name, String description) {
        record.setName(name);
        record.setDescription(description);
        return record;
    }
}
