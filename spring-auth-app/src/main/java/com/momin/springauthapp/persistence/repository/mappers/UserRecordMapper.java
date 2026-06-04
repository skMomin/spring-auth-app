package com.momin.springauthapp.persistence.repository.mappers;

import com.momin.springauthapp.jooq.tables.records.UsersRecord;
import com.momin.springauthapp.user.UserAccount;
import com.momin.springauthapp.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserRecordMapper {

    public UserDto toDto(UsersRecord record) {
        return new UserDto(
            record.getId(),
            record.getEmail(),
            record.getCreatedAt()
        );
    }

    public UserAccount toAccount(UsersRecord record) {
        return new UserAccount(
            record.getId(),
            record.getEmail(),
            record.getPasswordHash(),
            record.getCreatedAt()
        );
    }

    public UsersRecord populateForCreate(UsersRecord record, String email, String passwordHash) {
        record.setEmail(email);
        record.setPasswordHash(passwordHash);
        return record;
    }
}
