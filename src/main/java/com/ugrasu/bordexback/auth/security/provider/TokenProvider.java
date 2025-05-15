package com.ugrasu.bordexback.auth.security.provider;

import com.ugrasu.bordexback.auth.dto.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.jwt.JwtEncoderParameters.from;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExploitation;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExploitation;

    private final JwtEncoder jwtEncoder;

    public Tokens generateTokens(UserDetails user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return Tokens.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpires(accessTokenExploitation)
                .refreshTokenExpires(refreshTokenExploitation)
                .build();
    }

    private String generateAccessToken(UserDetails user) {
        Set<String> roles = getUserCorrectRoles(user);
        String username = user.getUsername();
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .claim("token-type", "access_token")
                .issuer("self")
                .subject(username)
                .claim("roles", roles)
                .expiresAt(now.plus(accessTokenExploitation, ChronoUnit.MINUTES))
                .issuedAt(now)
                .build();
        return jwtEncoder.encode(from(claims))
                .getTokenValue();
    }

    private String generateRefreshToken(UserDetails user) {
        String username = user.getUsername();
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .claim("token-type", "refresh_token")
                .subject(username)
                .issuer("self")
                .expiresAt(now.plus(refreshTokenExploitation, ChronoUnit.MINUTES))
                .issuedAt(now)
                .build();
        return jwtEncoder.encode(from(claims))
                .getTokenValue();
    }

    private Set<String> getUserCorrectRoles(UserDetails user) {
        return user.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority
                        .getAuthority()
                        .replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }
}
