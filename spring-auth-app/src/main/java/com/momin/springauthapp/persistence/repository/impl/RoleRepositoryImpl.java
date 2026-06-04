package com.momin.springauthapp.persistence.repository.impl;

import static com.momin.springauthapp.jooq.tables.Roles.ROLES;
import static com.momin.springauthapp.jooq.tables.UserRoles.USER_ROLES;

import com.momin.springauthapp.persistence.repository.RoleRepository;
import com.momin.springauthapp.persistence.repository.mappers.RoleRecordMapper;
import java.util.List;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private final DSLContext dsl;
    private final RoleRecordMapper roleRecordMapper;

    public RoleRepositoryImpl(DSLContext dsl, RoleRecordMapper roleRecordMapper) {
        this.dsl = dsl;
        this.roleRecordMapper = roleRecordMapper;
    }

    @Override
    public void assignRole(UUID userId, String code) {
        var roleId = dsl.select(ROLES.ID)
            .from(ROLES)
            .where(ROLES.CODE.eq(code))
            .fetchOptional(ROLES.ID)
            .orElseThrow(() -> new IllegalStateException("Role not found: " + code));

        var record = roleRecordMapper.populateForAssignment(dsl.newRecord(USER_ROLES), roleId);
        record.setUserId(userId);

        dsl.insertInto(USER_ROLES)
            .set(record)
            .onConflictDoNothing()
            .execute();
    }

    @Override
    public List<String> findRoleCodesByUserId(UUID userId) {
        return dsl.select(ROLES.CODE)
            .from(USER_ROLES)
            .join(ROLES).on(USER_ROLES.ROLE_ID.eq(ROLES.ID))
            .where(USER_ROLES.USER_ID.eq(userId))
            .orderBy(ROLES.CODE.asc())
            .fetch(ROLES.CODE);
    }
}
