package com.plataforma_academica.plataforma.academic.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sala_de_aula")
@Getter
@Setter
public class SaladeAulaEntity {
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_sala", unique = true, nullable = false, length = 8)
    private String codigoSala;

    @Column(name = "criador_id", nullable = false)
    private UUID criadorId;

    @ElementCollection
    @CollectionTable(name = "sala_membros", joinColumns = @JoinColumn(name = "sala_id"))
    @Column(name = "usuario_id")
    private List<UUID> membrosIds;
}
