package com.plataforma_academica.plataforma.dto;

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

    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime criadoEm;

    // Informações do dono da comunidade
    private Long donoId;
    private String donoNome;
}
