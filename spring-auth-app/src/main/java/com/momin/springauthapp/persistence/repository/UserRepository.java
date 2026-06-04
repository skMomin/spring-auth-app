package com.momin.springauthapp.persistence.repository;

import com.momin.springauthapp.user.UserAccount;
import com.momin.springauthapp.user.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    List<UserDto> findAll();

    Optional<UserDto> findByEmail(String email);

    Optional<UserAccount> findAccountByEmail(String email);

    Optional<UserDto> findById(UUID id);

    UserDto create(String email, String passwordHash);
}
