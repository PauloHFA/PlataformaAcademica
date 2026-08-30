package com.plataforma_academica.plataforma.dto;

import java.util.UUID;

import lombok.Data;
import java.util.List;

/**
 * DTO de transferÃªncia para Salas de Aula.
 * 
 * Camada: Presentation / DTO (Academic Context)
 * 
 * @see SaladeAula
 * @see SalaDeAulaMapper
 */
@Data
public class SalaDeAulaDTO {
    private UUID id;
    private String nome;
    private UUID criadorId; // evita enviar Usuario inteiro
    private List<UUID> membrosIds; // lista simples
    private List<UUID> atividadesIds; // leve
}

