package com.nashTech.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockApiApplication {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Mock API for Docker Demo...");
        SpringApplication.run(MockApiApplication.class, args);
    }
}
