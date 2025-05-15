package com.ugrasu.bordexback.rest.controller.expression;

import com.ugrasu.bordexback.auth.security.authenfication.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityExpressionPrincipal {

    protected AuthenticatedUser getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (AuthenticatedUser) authentication.getPrincipal();
    }
}
