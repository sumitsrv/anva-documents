package com.anva.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class to bootstrap the Spring Boot application.
 */
@SpringBootApplication(scanBasePackages = "com.anva")
public class ApplicationBoot {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationBoot.class, args);
    }
}