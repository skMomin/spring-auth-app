package com.momin.springauthapp.service;

import com.momin.springauthapp.user.UserDto;
import java.util.List;

public interface AuthService {

    AuthResponse register(String email, String rawPassword);

    AuthResponse login(String email, String rawPassword);

    AuthResponse refresh(String refreshToken);

    record AuthResponse(
        UserDto user,
        List<String> roles,
        String accessToken,
        String accessTokenExpiresAt,
        String refreshToken,
        String refreshTokenExpiresAt
    ) {
    }
}
