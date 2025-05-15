package com.ugrasu.bordexback.auth.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CookieJwtAuthenticationFilter extends OncePerRequestFilter {

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

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName()) || "refresh_token".equals(cookie.getName())) {
                    request = getHttpServletRequest(request, cookie);
                    break;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static @NotNull HttpServletRequest getHttpServletRequest(HttpServletRequest request, Cookie cookie) {
        String token = cookie.getValue();
        request = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if ("Authorization".equals(name)) {
                    return "Bearer " + token;
                }
                return super.getHeader(name);
            }
        };
        return request;
    }

}
