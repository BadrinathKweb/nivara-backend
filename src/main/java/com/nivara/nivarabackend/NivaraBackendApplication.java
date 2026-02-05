package com.nivara.nivarabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nivara.nivarabackend")
public class NivaraBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NivaraBackendApplication.class, args);
    }
}
