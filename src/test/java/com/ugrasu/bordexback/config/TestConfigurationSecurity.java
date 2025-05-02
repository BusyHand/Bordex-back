package com.ugrasu.bordexback.config;

import com.ugrasu.bordexback.auth.config.CustomJwtAuthenticationConverter;
import com.ugrasu.bordexback.auth.config.CustomUserDetailService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;


@TestConfiguration
public class TestConfigurationSecurity {

}
