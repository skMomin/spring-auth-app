package com.momin.springauthapp.service.impl;

import com.momin.springauthapp.auth.AccessTokenService;
import com.momin.springauthapp.auth.RefreshTokenIssuer;
import com.momin.springauthapp.auth.TokenHasher;
import com.momin.springauthapp.persistence.repository.RefreshTokenRepository;
import com.momin.springauthapp.persistence.repository.RoleRepository;
import com.momin.springauthapp.persistence.repository.UserRepository;
import com.momin.springauthapp.service.AuthService;
import com.momin.springauthapp.user.UserDto;
import java.time.Clock;
import java.time.OffsetDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenIssuer refreshTokenIssuer;
    private final TokenHasher tokenHasher;
    private final AccessTokenService accessTokenService;
    private final Clock clock;

    public AuthServiceImpl(
        UserRepository userRepository,
        RoleRepository roleRepository,
        RefreshTokenRepository refreshTokenRepository,
        PasswordEncoder passwordEncoder,
        RefreshTokenIssuer refreshTokenIssuer,
        TokenHasher tokenHasher,
        AccessTokenService accessTokenService,
        Clock clock
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenIssuer = refreshTokenIssuer;
        this.tokenHasher = tokenHasher;
        this.accessTokenService = accessTokenService;
        this.clock = clock;
    }

    @Override
    @Transactional
    public AuthResponse register(String email, String rawPassword) {
        if (userRepository.findAccountByEmail(email).isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Email already registered");
        }

        var user = userRepository.create(email, passwordEncoder.encode(rawPassword));
        roleRepository.assignRole(user.id(), "USER");
        return issueTokens(user);
    }

    @Override
    @Transactional
    public AuthResponse login(String email, String rawPassword) {
        var account = userRepository.findAccountByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, account.passwordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid credentials");
        }

        var user = new UserDto(account.id(), account.email(), account.createdAt());
        return issueTokens(user);
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        var now = OffsetDateTime.now(clock);
        var currentToken = refreshTokenRepository.findActiveByHash(tokenHasher.hash(refreshToken), now)
            .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid refresh token"));

        refreshTokenRepository.revoke(currentToken.id(), now);

        var user = userRepository.findById(currentToken.userId())
            .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));

        return issueTokens(user);
    }

    private AuthResponse issueTokens(UserDto user) {
        var roles = roleRepository.findRoleCodesByUserId(user.id());
        var accessToken = accessTokenService.issueToken(user.id(), user.email(), roles);
        var issuedRefreshToken = refreshTokenIssuer.issue();

        refreshTokenRepository.create(
            user.id(),
            tokenHasher.hash(issuedRefreshToken.value()),
            issuedRefreshToken.expiresAt()
        );

        return new AuthResponse(
            user,
            roles,
            accessToken.value(),
            accessToken.expiresAt().toString(),
            issuedRefreshToken.value(),
            issuedRefreshToken.expiresAt().toString()
        );
    }
}
