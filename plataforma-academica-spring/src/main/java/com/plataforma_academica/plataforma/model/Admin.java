package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
@Data
@Getter
@Setter
public class Admin extends Usuario {

    // Admin tem acesso a tudo, sem campos adicionais
}