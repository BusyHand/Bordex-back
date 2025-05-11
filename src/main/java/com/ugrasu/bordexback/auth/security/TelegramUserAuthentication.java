package com.ugrasu.bordexback.auth.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TelegramUserAuthentication extends AbstractAuthenticationToken {

    private final Object principal;

    public TelegramUserAuthentication(String telegramPasscode) {
        super(null);
        this.principal = telegramPasscode;
        setAuthenticated(false);
    }

    public TelegramUserAuthentication(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
