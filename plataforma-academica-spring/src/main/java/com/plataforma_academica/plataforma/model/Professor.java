package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entidade JPA que representa um Professor na plataforma.
 * 
 * Camada: Persistence / JPA Entity (Identity Context)
 * Herança: Estende {@link Usuario} através de JOINED (tabela filha `professor`),
 * adicionando atributos acadêmicos específicos como a matrícula.
 * 
 * @see docs/domain/identity_context.md
 * @see REQ-005 (Gestão de Papéis Docentes)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "professor")
public class Professor extends Usuario {

    /** Matrícula institucional única do professor. */
    @JsonProperty("matricula")
    @Column(nullable = false, length = 8)
    private String matricula;
}

    @JsonProperty("matricula")
    @Column(nullable = false, length = 8)
    private String matricula;
}
