package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.util.List;

/**
 * DTO de resposta para Salas de Aula.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SaladeAula
 * @see SalaDeAulaMapper
 */
@Data
public class SalaDeAulaResponseDTO {

    private UUID id;
    private String nome;

    private UUID criadorId;
    private String criadorNome; // nome do criador (Ãºtil para exibiÃ§Ã£o)

    private List<UUID> membrosIds;
    private List<String> membrosNomes; // nomes dos membros â€“ opcional mas muito Ãºtil

    private List<UUID> atividadesIds;
}

