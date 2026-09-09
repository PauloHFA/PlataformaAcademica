package com.plataforma_academica.plataforma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.plataforma_academica.plataforma")
@EnableJpaRepositories(basePackages = {
    "com.plataforma_academica.plataforma.repository",
    "com.plataforma_academica.plataforma.identity.infrastructure.persistence"
})
public class PlataformaAcademicaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlataformaAcademicaApplication.class, args);
    }
}
