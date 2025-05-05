package com.ugrasu.bordexback;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class BordexBackApplication {

    //todo доски по userId
    //


    public static void main(String[] args) {
        SpringApplication.run(BordexBackApplication.class, args);
    }
}
