package com.momin.springauthapp.auth;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final AuthProperties authProperties;
    private final Clock clock;

    public AccessTokenService(AuthProperties authProperties, Clock clock) {
        this.authProperties = authProperties;
        this.clock = clock;
    }

    public AccessToken issueToken(UUID userId, String email, List<String> roles) {
        var issuedAt = clock.instant();
        var expiresAt = issuedAt.plus(authProperties.accessTokenTtl());
        var rolesJson = roles.stream()
            .map(this::escape)
            .map(role -> "\"" + role + "\"")
            .toList();

        var header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        var payload = base64Url("""
            {"sub":"%s","email":"%s","roles":[%s],"iat":%d,"exp":%d}
            """.formatted(
            userId,
            escape(email),
            String.join(",", rolesJson),
            issuedAt.getEpochSecond(),
            expiresAt.getEpochSecond()
        ).replace("\n", "").trim());
        var signature = sign(header + "." + payload);

        return new AccessToken(header + "." + payload + "." + signature, expiresAt);
    }

    private String sign(String data) {
        try {
            var mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(authProperties.jwtSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return BASE64_URL_ENCODER.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to sign access token", exception);
        }
    }

    private String base64Url(String value) {
        return BASE64_URL_ENCODER.encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public record AccessToken(String value, Instant expiresAt) {
    }
}
