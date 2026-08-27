package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade JPA que representa um Administrador no sistema.
 * 
 * Camada: Persistence / JPA Entity
 * Estende a entidade {@link Usuario} através de herança
 * (DiscriminatorValue("ADMIN")),
 * possuindo privilégios elevados de gerenciamento sem campos adicionais na
 * tabela específica.
 */
@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@Data
@Getter
@Setter
public class Admin extends Usuario {

    // Admin tem acesso a tudo, sem campos adicionais
}