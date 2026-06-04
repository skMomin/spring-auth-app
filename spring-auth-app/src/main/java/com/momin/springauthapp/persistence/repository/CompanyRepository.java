package com.momin.springauthapp.persistence.repository;

import com.momin.springauthapp.company.CompanyDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    CompanyDto create(UUID ownerId, String name, String description);

    List<CompanyDto> findByOwnerId(UUID ownerId);

    Optional<CompanyDto> findById(UUID id);

    Optional<CompanyDto> findByIdAndOwnerId(UUID id, UUID ownerId);

    CompanyDto update(UUID id, String name, String description);

    void delete(UUID id);
}
