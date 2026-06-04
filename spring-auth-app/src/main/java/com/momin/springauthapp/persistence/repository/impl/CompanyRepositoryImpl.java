package com.momin.springauthapp.persistence.repository.impl;

import static com.momin.springauthapp.jooq.tables.Companies.COMPANIES;

import com.momin.springauthapp.company.CompanyDto;
import com.momin.springauthapp.persistence.repository.CompanyRepository;
import com.momin.springauthapp.persistence.repository.mappers.CompanyRecordMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    private final DSLContext dsl;
    private final CompanyRecordMapper companyRecordMapper;

    public CompanyRepositoryImpl(DSLContext dsl, CompanyRecordMapper companyRecordMapper) {
        this.dsl = dsl;
        this.companyRecordMapper = companyRecordMapper;
    }

    @Override
    public CompanyDto create(UUID ownerId, String name, String description) {
        var record = companyRecordMapper.populateForCreate(
            dsl.newRecord(COMPANIES),
            ownerId,
            name,
            description
        );
        record.setId(UUID.randomUUID());
        record.store();
        record.refresh();
        return companyRecordMapper.toDto(record);
    }

    @Override
    public List<CompanyDto> findByOwnerId(UUID ownerId) {
        return dsl.selectFrom(COMPANIES)
            .where(COMPANIES.OWNER_ID.eq(ownerId))
            .orderBy(COMPANIES.CREATED_AT.desc())
            .fetch(companyRecordMapper::toDto);
    }

    @Override
    public Optional<CompanyDto> findById(UUID id) {
        return dsl.selectFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))
            .fetchOptional(companyRecordMapper::toDto);
    }

    @Override
    public Optional<CompanyDto> findByIdAndOwnerId(UUID id, UUID ownerId) {
        return dsl.selectFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))
            .and(COMPANIES.OWNER_ID.eq(ownerId))
            .fetchOptional(companyRecordMapper::toDto);
    }

    @Override
    public CompanyDto update(UUID id, String name, String description) {
        var record = dsl.selectFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))
            .fetchOne();

        if (record == null) {
            throw new IllegalStateException("Company not found: " + id);
        }

        companyRecordMapper.mergeForUpdate(record, name, description);
        record.store();
        record.refresh();
        return companyRecordMapper.toDto(record);
    }

    @Override
    public void delete(UUID id) {
        dsl.deleteFrom(COMPANIES)
            .where(COMPANIES.ID.eq(id))
            .execute();
    }
}
