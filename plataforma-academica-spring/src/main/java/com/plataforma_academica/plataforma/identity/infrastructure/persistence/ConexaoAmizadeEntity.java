package com.plataforma_academica.plataforma.identity.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "conexao_amizade")
public class ConexaoAmizadeEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String solicitanteId;

    @Column(nullable = false)
    private String destinatarioId;

    @Column(nullable = false)
    private String status;

    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataResposta;
}
