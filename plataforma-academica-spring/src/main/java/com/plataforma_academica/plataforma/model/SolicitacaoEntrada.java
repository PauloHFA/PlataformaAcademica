package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitacao_entrada")
public class SolicitacaoEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private SaladeAula sala;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status = StatusSolicitacao.PENDENTE;

    @Column(nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    private LocalDateTime dataResposta;

    public enum StatusSolicitacao {
        PENDENTE, APROVADA, REJEITADA
    }
}
