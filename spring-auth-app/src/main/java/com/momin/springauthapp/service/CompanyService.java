package com.momin.springauthapp.service;

import com.momin.springauthapp.company.CompanyDto;
import java.util.List;
import java.util.UUID;

public interface CompanyService {

    CompanyDto create(UUID ownerId, String name, String description);

    List<CompanyDto> findByOwnerId(UUID ownerId);

    CompanyDto findById(UUID id);

    CompanyDto update(UUID ownerId, UUID companyId, String name, String description);

    void delete(UUID ownerId, UUID companyId);
}
