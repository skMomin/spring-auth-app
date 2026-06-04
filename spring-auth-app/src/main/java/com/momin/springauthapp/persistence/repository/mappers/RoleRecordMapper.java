package com.momin.springauthapp.persistence.repository.mappers;

import com.momin.springauthapp.jooq.tables.records.UserRolesRecord;
import org.springframework.stereotype.Component;

@Component
public class RoleRecordMapper {

    public UserRolesRecord populateForAssignment(UserRolesRecord record, long roleId) {
        record.setRoleId(roleId);
        return record;
    }
}
