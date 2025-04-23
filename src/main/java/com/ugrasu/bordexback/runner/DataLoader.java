package com.ugrasu.bordexback.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}
