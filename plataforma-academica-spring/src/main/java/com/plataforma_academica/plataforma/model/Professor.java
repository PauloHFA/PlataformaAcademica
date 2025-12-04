package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "professor")
public class Professor extends Usuario {

    @JsonProperty("matricula")
    @Column(nullable = false, length = 8)
    private String matricula;
}
