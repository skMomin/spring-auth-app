package com.momin.springauthapp.persistence.repository.impl;

import static com.momin.springauthapp.jooq.tables.Users.USERS;

import com.momin.springauthapp.persistence.repository.UserRepository;
import com.momin.springauthapp.persistence.repository.mappers.UserRecordMapper;
import com.momin.springauthapp.user.UserAccount;
import com.momin.springauthapp.user.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;
    private final UserRecordMapper userRecordMapper;

    public UserRepositoryImpl(DSLContext dsl, UserRecordMapper userRecordMapper) {
        this.dsl = dsl;
        this.userRecordMapper = userRecordMapper;
    }

    @Override
    public List<UserDto> findAll() {
        return dsl.selectFrom(USERS)
            .orderBy(USERS.CREATED_AT.desc())
            .fetch(userRecordMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return dsl.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOptional(userRecordMapper::toDto);
    }

    @Override
    public Optional<UserAccount> findAccountByEmail(String email) {
        return dsl.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOptional(userRecordMapper::toAccount);
    }

    @Override
    public Optional<UserDto> findById(UUID id) {
        return dsl.selectFrom(USERS)
            .where(USERS.ID.eq(id))
            .fetchOptional(userRecordMapper::toDto);
    }

    @Override
    public UserDto create(String email, String passwordHash) {
        var record = userRecordMapper.populateForCreate(dsl.newRecord(USERS), email, passwordHash);
        record.setId(UUID.randomUUID());
        record.store();
        record.refresh();
        return userRecordMapper.toDto(record);
    }
}
