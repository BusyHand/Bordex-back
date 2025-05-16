package com.ugrasu.bordexback.auth.security.filter;

import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.security.JwtCookieComponent;
import com.ugrasu.bordexback.auth.security.authenfication.AuthenticatedUser;
import com.ugrasu.bordexback.auth.security.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class RefreshCookieFilter extends OncePerRequestFilter {

    private final JwtCookieComponent jwtCookieComponent;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if ("/api/auth/logout".equals(path) ||
            "/api/auth/register".equals(path) ||
            "/api/auth/login".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        Set<String> tokens = new HashSet<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    tokens.add(cookie.getName());
                }
                if ("refresh_token".equals(cookie.getName())) {
                    tokens.add(cookie.getName());
                }
            }
        }
        if (tokens.size() == 1 && tokens.contains("refresh_token")) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AbstractAuthenticationToken) {
                var loginUser = (AuthenticatedUser) authentication.getPrincipal();
                Tokens tokens1 = tokenProvider.generateTokens(loginUser);
                jwtCookieComponent.addJwtSetCookies(response, tokens1);
            }
        }
        filterChain.doFilter(request, response);
    }
}