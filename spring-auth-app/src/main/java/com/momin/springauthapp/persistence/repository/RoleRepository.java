package com.momin.springauthapp.persistence.repository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository {

    void assignRole(UUID userId, String code);

    List<String> findRoleCodesByUserId(UUID userId);
}
