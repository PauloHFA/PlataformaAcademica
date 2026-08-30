package com.plataforma_academica.plataforma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.plataforma_academica.plataforma")
public class PlataformaAcademicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaAcademicaApplication.class, args);
    }
}
