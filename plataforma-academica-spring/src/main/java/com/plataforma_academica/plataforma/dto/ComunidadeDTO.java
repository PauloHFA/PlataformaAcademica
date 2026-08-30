package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferÃªncia para criaÃ§Ã£o/atualizaÃ§Ã£o de Comunidades.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Comunidade
 * @see ComunidadeMapper
 */
@Data
public class ComunidadeDTO {

    private UUID id;
    private String nome;
    private String descricao;

    private LocalDateTime criadoEm;

    private UUID donoId;
}
