package com.momin.springauthapp.service.impl;

import com.momin.springauthapp.company.CompanyDto;
import com.momin.springauthapp.persistence.repository.CompanyRepository;
import com.momin.springauthapp.persistence.repository.UserRepository;
import com.momin.springauthapp.service.CompanyService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CompanyDto create(UUID ownerId, String name, String description) {
        userRepository.findById(ownerId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Owner not found"));

        return companyRepository.create(ownerId, name, description);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDto> findByOwnerId(UUID ownerId) {
        userRepository.findById(ownerId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Owner not found"));

        return companyRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDto findById(UUID id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Company not found"));
    }

    @Override
    @Transactional
    public CompanyDto update(UUID ownerId, UUID companyId, String name, String description) {
        ensureOwnerExists(ownerId);
        ensureOwnership(companyId, ownerId);
        return companyRepository.update(companyId, name, description);
    }

    @Override
    @Transactional
    public void delete(UUID ownerId, UUID companyId) {
        ensureOwnerExists(ownerId);
        ensureOwnership(companyId, ownerId);
        companyRepository.delete(companyId);
    }

    private void ensureOwnerExists(UUID ownerId) {
        userRepository.findById(ownerId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Owner not found"));
    }

    private void ensureOwnership(UUID companyId, UUID ownerId) {
        if (companyRepository.findById(companyId).isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Company not found");
        }

        if (companyRepository.findByIdAndOwnerId(companyId, ownerId).isEmpty()) {
            throw new ResponseStatusException(FORBIDDEN, "You do not own this company");
        }
    }
}
