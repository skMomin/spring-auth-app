package com.momin.springauthapp.endpoint;

import com.momin.springauthapp.company.CompanyDto;
import com.momin.springauthapp.service.CompanyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDto create(@Valid @RequestBody CreateCompanyRequest request) {
        return companyService.create(request.ownerId(), request.name(), request.description());
    }

    @GetMapping("/companies/{companyId}")
    public CompanyDto findById(@PathVariable UUID companyId) {
        return companyService.findById(companyId);
    }

    @PutMapping("/companies/{companyId}")
    public CompanyDto update(@PathVariable UUID companyId, @Valid @RequestBody UpdateCompanyRequest request) {
        return companyService.update(request.ownerId(), companyId, request.name(), request.description());
    }

    @DeleteMapping("/companies/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID companyId, @RequestParam UUID ownerId) {
        companyService.delete(ownerId, companyId);
    }

    @GetMapping("/users/{ownerId}/companies")
    public List<CompanyDto> findByOwnerId(@PathVariable UUID ownerId) {
        return companyService.findByOwnerId(ownerId);
    }

    public record CreateCompanyRequest(
        @NotNull UUID ownerId,
        @NotBlank String name,
        String description
    ) {
    }

    public record UpdateCompanyRequest(
        @NotNull UUID ownerId,
        @NotBlank String name,
        String description
    ) {
    }
}
