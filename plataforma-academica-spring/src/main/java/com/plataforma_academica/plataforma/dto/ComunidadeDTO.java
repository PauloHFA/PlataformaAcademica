package com.plataforma_academica.plataforma.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO de transferência para criação/atualização de Comunidades.
 * 
 * Camada: Presentation / DTO (Social Context)
 * 
 * @see Comunidade
 * @see ComunidadeMapper
 */
@Data
public class ComunidadeDTO {

    private Long id;
    private String nome;
    private String descricao;

    private LocalDateTime criadoEm;

    private Long donoId;
}