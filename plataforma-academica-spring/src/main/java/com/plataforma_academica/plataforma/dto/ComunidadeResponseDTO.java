package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de resposta para Comunidades.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Comunidade
 * @see ComunidadeMapper
 */
@Data
public class ComunidadeResponseDTO {

    private UUID id;
    private String nome;
    private String descricao;
    private LocalDateTime criadoEm;

    // InformaÃ§Ãµes do dono da comunidade
    private UUID donoId;
    private String donoNome;
}

